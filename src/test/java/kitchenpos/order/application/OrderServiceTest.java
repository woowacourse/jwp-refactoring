package kitchenpos.order.application;

import static kitchenpos.test.fixture.MenuFixture.메뉴;
import static kitchenpos.test.fixture.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.test.fixture.ProductFixture.상품;
import static kitchenpos.test.fixture.TableFixture.테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.menu.application.MenuProductMapper;
import kitchenpos.menu.application.dto.MenuProductQuantityDto;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.vo.OrderStatus;
import kitchenpos.order.dto.request.OrderCreateRequest;
import kitchenpos.order.dto.request.OrderLineItemRequest;
import kitchenpos.order.dto.request.OrderUpdateStatusRequest;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
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
    private MenuProductMapper menuProductMapper;

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
            Menu menuFixture = 메뉴("텐동",
                    BigDecimal.valueOf(11000),
                    menuGroup,
                    List.of(new MenuProductQuantityDto(product.getId(), 1)),
                    menuProductMapper
            );
            Menu menu = menuRepository.save(menuFixture);

            OrderTable orderTable = orderTableRepository.save(테이블(10, false));
            OrderCreateRequest request = new OrderCreateRequest(
                    orderTable.getId(),
                    null,
                    List.of(new OrderLineItemRequest(menu.getId(), 1L))
            );
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
            Menu menuFixture = 메뉴(
                    "텐동",
                    BigDecimal.valueOf(11000),
                    menuGroup,
                    List.of(new MenuProductQuantityDto(product.getId(), 1)),
                    menuProductMapper
            );
            menu = menuRepository.save(menuFixture);
        }

        @Test
        void 정상적인_주문이라면_주문을_추가한다() {
            //given
            OrderTable orderTable = orderTableRepository.save(테이블(10, false));
            OrderCreateRequest request = new OrderCreateRequest(
                    orderTable.getId(),
                    null,
                    List.of(new OrderLineItemRequest(menu.getId(), 1L))
            );

            //when
            LocalDateTime now = LocalDateTime.now();
            OrderResponse response = orderService.create(request);

            //then
            assertSoftly(softly -> {
                softly.assertThat(response.getId()).isNotNull();
                softly.assertThat(response.getOrderTableId()).isEqualTo(orderTable.getId());
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
            OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(), null, Collections.emptyList());

            //when, then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_메뉴_목록에_메뉴가_존재하지_않으면_예외를_던진다() {
            //given
            OrderTable orderTable = orderTableRepository.save(테이블(10, false));
            OrderCreateRequest request = new OrderCreateRequest(
                    orderTable.getId(),
                    null,
                    List.of(new OrderLineItemRequest(menu.getId() + 1, 1L))
            );

            //when, then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_테이블이라면_예외를_던진다() {
            //given
            OrderCreateRequest request = new OrderCreateRequest(
                    -1L,
                    null,
                    List.of(new OrderLineItemRequest(menu.getId(), 1L))
            );

            //when, then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블이_비어있으면_예외를_던진다() {
            //given
            OrderTable orderTable = orderTableRepository.save(테이블(0, true));
            OrderCreateRequest request = new OrderCreateRequest(
                    orderTable.getId(),
                    null,
                    List.of(new OrderLineItemRequest(menu.getId(), 1L))
            );

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
            Menu menuFixture = 메뉴(
                    "텐동",
                    BigDecimal.valueOf(11000),
                    menuGroup,
                    List.of(new MenuProductQuantityDto(product.getId(), 1)),
                    menuProductMapper
            );
            Menu menu = menuRepository.save(menuFixture);

            OrderTable orderTable = orderTableRepository.save(테이블(10, false));
            OrderCreateRequest request = new OrderCreateRequest(
                    orderTable.getId(),
                    null,
                    List.of(new OrderLineItemRequest(menu.getId(), 1L))
            );
            OrderResponse orderResponse = orderService.create(request);
            orderId = orderResponse.getId();
        }

        @Test
        void 정상적인_주문이라면_주문_상태를_수정한다() {
            //given
            OrderUpdateStatusRequest request = new OrderUpdateStatusRequest(OrderStatus.MEAL.name());

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
            OrderUpdateStatusRequest request = new OrderUpdateStatusRequest(OrderStatus.MEAL.name());

            //when, then
            assertThatThrownBy(() -> orderService.changeOrderStatus(-1L, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 완료된_주문이라면_예외를_던진다() {
            //given
            OrderUpdateStatusRequest completeRequest = new OrderUpdateStatusRequest(OrderStatus.COMPLETION.name());
            orderService.changeOrderStatus(orderId, completeRequest);
            OrderUpdateStatusRequest request = new OrderUpdateStatusRequest(OrderStatus.MEAL.name());

            //when, then
            assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
