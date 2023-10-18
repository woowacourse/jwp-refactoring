package kitchenpos.application;

import static kitchenpos.test.fixture.MenuFixture.메뉴;
import static kitchenpos.test.fixture.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.test.fixture.MenuProductFixture.메뉴_상품;
import static kitchenpos.test.fixture.ProductFixture.상품;
import static kitchenpos.test.fixture.TableFixture.테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProductRepository;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.domain.vo.OrderStatus;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.test.ServiceTest;
import kitchenpos.test.TestTransactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TestTransactional testTransactional;

    @Nested
    class 주문_목록_조회_시 {

        @Test
        void 모든_주문_목록을_조회한다() {
            //given
            Product product = productRepository.save(상품("텐동", BigDecimal.valueOf(11000)));
            MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹("일식"));
            Menu menu = menuRepository.save(메뉴("텐동", BigDecimal.valueOf(11000), menuGroup));
            menuProductRepository.save(메뉴_상품(menu, product, 1));

            OrderTable orderTable = orderTableRepository.save(테이블(10, false));
            OrderRequest request =
                    new OrderRequest(orderTable.getId(), null, List.of(new OrderLineItemRequest(menu.getId(), 1L)));
            OrderResponse orderResponseA = orderService.create(request);
            OrderResponse orderResponseB = orderService.create(request);

            //when
            List<OrderResponse> orders = orderService.list();

            //then
            assertThat(orders).usingRecursiveComparison().isEqualTo(List.of(orderResponseA, orderResponseB));
        }

        @Test
        void 주문이_존재하지_않으면_목록이_비어있다() {
            //given, when
            List<OrderResponse> orders = orderService.list();

            //then
            assertThat(orders).isEmpty();
        }
    }

    @Nested
    class 주문_추가_시 {

        private Menu menu;

        @BeforeEach
        void setUp() {
            Product product = productRepository.save(상품("텐동", BigDecimal.valueOf(11000)));
            MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹("일식"));
            menu = menuRepository.save(메뉴("텐동", BigDecimal.valueOf(11000), menuGroup));
            menuProductRepository.save(메뉴_상품(menu, product, 1));
        }

        @Test
        void 정상적인_주문이라면_주문을_추가한다() {
            //given
            OrderTable orderTable = orderTableRepository.save(테이블(10, false));
            OrderRequest request =
                    new OrderRequest(orderTable.getId(), null, List.of(new OrderLineItemRequest(menu.getId(), 1L)));

            //when
            LocalDateTime now = LocalDateTime.now();
            OrderResponse response = orderService.create(request);

            //then
            assertSoftly(softly -> {
                softly.assertThat(response.getId()).isNotNull();
                softly.assertThat(response.getOrderTable().getId()).isEqualTo(orderTable.getId());
                softly.assertThat(response.getOrderTable().getNumberOfGuests())
                        .isEqualTo(orderTable.getNumberOfGuests());
                softly.assertThat(response.getOrderTable().isEmpty()).isEqualTo(orderTable.isEmpty());
                softly.assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
                softly.assertThat(response.getOrderedTime()).isAfter(now);
                softly.assertThat(response.getOrderLineItems()).hasSize(1);
                softly.assertThat(response.getOrderLineItems().get(0).getMenuId()).isEqualTo(menu.getId());
                softly.assertThat(response.getOrderLineItems().get(0).getQuantity()).isEqualTo(1L);
            });
        }

        @Test
        void 주문_메뉴_목록이_비어있으면_예외를_던진다() {
            //given
            OrderTable orderTable = orderTableRepository.save(테이블(10, false));
            OrderRequest request = new OrderRequest(orderTable.getId(), null, Collections.emptyList());

            //when, then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_메뉴_목록에_메뉴가_존재하지_않으면_예외를_던진다() {
            //given
            OrderTable orderTable = orderTableRepository.save(테이블(10, false));
            OrderRequest request =
                    new OrderRequest(orderTable.getId(), null, List.of(new OrderLineItemRequest(menu.getId() + 1, 1L)));

            //when, then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_테이블이라면_예외를_던진다() {
            //given
            OrderRequest request = new OrderRequest(-1L, null, List.of(new OrderLineItemRequest(menu.getId(), 1L)));

            //when, then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블이_비어있으면_예외를_던진다() {
            //given
            OrderTable orderTable = orderTableRepository.save(테이블(0, true));
            OrderRequest request =
                    new OrderRequest(orderTable.getId(), null, List.of(new OrderLineItemRequest(menu.getId(), 1L)));

            //when, then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 주문_상태_수정_시 {

        private Long orderId;

        @BeforeEach
        void setUp() {
            Product product = productRepository.save(상품("텐동", BigDecimal.valueOf(11000)));
            MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹("일식"));
            Menu menu = menuRepository.save(메뉴("텐동", BigDecimal.valueOf(11000), menuGroup));
            menuProductRepository.save(메뉴_상품(menu, product, 1));

            OrderTable orderTable = orderTableRepository.save(테이블(10, false));
            OrderRequest request =
                    new OrderRequest(orderTable.getId(), null, List.of(new OrderLineItemRequest(menu.getId(), 1L)));
            OrderResponse orderResponse = orderService.create(request);
            orderId = orderResponse.getId();
        }

        @Test
        void 정상적인_주문이라면_주문_상태를_수정한다() {
            //given
            OrderRequest request = new OrderRequest(null, OrderStatus.MEAL.name(), Collections.emptyList());

            //when
            OrderResponse response = orderService.changeOrderStatus(orderId, request);

            //then
            testTransactional.invoke(() -> {
                Order order = orderRepository.findById(orderId).get();
                assertSoftly(softly -> {
                    softly.assertThat(response).usingRecursiveComparison()
                            .ignoringFields("orderStatus")
                            .isEqualTo(OrderResponse.from(order));
                    softly.assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
                });
            });
        }

        @Test
        void 존재하지_않는_주문이라면_예외를_던진다() {
            //given
            OrderRequest request = new OrderRequest(null, OrderStatus.MEAL.name(), Collections.emptyList());

            //when, then
            assertThatThrownBy(() -> orderService.changeOrderStatus(-1L, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 완료된_주문이라면_예외를_던진다() {
            //given
            OrderRequest completeRequest = new OrderRequest(null, OrderStatus.COMPLETION.name(),
                    Collections.emptyList());
            orderService.changeOrderStatus(orderId, completeRequest);
            OrderRequest request = new OrderRequest(null, OrderStatus.MEAL.name(), Collections.emptyList());

            //when, then
            assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
