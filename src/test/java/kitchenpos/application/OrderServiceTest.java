package kitchenpos.application;

import kitchenpos.application.dto.OrderCreateRequest;
import kitchenpos.application.dto.OrderCreateRequest.OrderLineItemRequest;
import kitchenpos.application.dto.OrderResponse;
import kitchenpos.application.dto.OrderStatusChangeRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static kitchenpos.fixture.MenuGroupFixtures.TEST_GROUP;
import static kitchenpos.fixture.OrderTableFixtures.EMPTY_TABLE;
import static kitchenpos.fixture.OrderTableFixtures.NOT_EMPTY_TABLE;
import static kitchenpos.fixture.ProductFixtures.PIZZA;

@Transactional
@SpringBootTest
class OrderServiceTest {

    private static final Long WRONG_ID = -1L;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    private OrderTable notEmptyTable;
    private Menu testMenu;

    @BeforeEach
    void setup() {
        notEmptyTable = orderTableRepository.save(NOT_EMPTY_TABLE());
        final MenuGroup savedMenuGroup = menuGroupRepository.save(TEST_GROUP());
        final Product savedProduct = productRepository.save(PIZZA());

        final Menu menu = new Menu.MenuFactory("테스트 메뉴", new Price(BigDecimal.valueOf(10000)), savedMenuGroup)
                .addProduct(savedProduct, 1)
                .create();

        testMenu = menuRepository.save(menu);
    }

    @Nested
    @DisplayName("주문 생성테스트")
    class CreateTest {

        @Test
        @DisplayName("주문 생성에 성공한다.")
        void success() {
            // given
            final OrderCreateRequest request = new OrderCreateRequest(notEmptyTable.getId(), List.of(new OrderLineItemRequest(testMenu.getId(), 1L)));

            // when
            final OrderResponse response = orderService.create(request);

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(response.getId()).isNotNull();
                softly.assertThat(response.getOrderStatus()).isEqualTo("COOKING");
            });
        }

        @Test
        @DisplayName("메뉴가 없이 주문을 하면 예외가 발생한다.")
        void throwExceptionWithEmptyMenuList() {
            // given
            final OrderCreateRequest request = new OrderCreateRequest(notEmptyTable.getId(), Collections.emptyList());

            // when
            // then
            Assertions.assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("잘못된 메뉴로 주문을 하면 예외가 발생한다.")
        void throwExcpetionWithWorngMenuId() {
            // given
            final OrderCreateRequest request = new OrderCreateRequest(notEmptyTable.getId(), List.of(new OrderLineItemRequest(WRONG_ID, 1L)));

            // when
            // then
            Assertions.assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("비어있는 테이블에서 주문 생성시 예외가 발생한다.")
        void throwExceptionWithEmptyTable() {
            // given
            final OrderTable savedEmptyTable = orderTableRepository.save(EMPTY_TABLE());

            final OrderCreateRequest request = new OrderCreateRequest(savedEmptyTable.getId(), List.of(new OrderLineItemRequest(testMenu.getId(), 1L)));

            // when
            // then
            Assertions.assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("주문 상태 변경 테스트")
    class ChangeStatusTest {

        private Order testOrder;

        @BeforeEach
        void setup() {
            final Order order = new Order.OrderFactory(notEmptyTable)
                    .addMenu(testMenu, 1L)
                    .create();
            testOrder = orderRepository.save(order);
        }

        @ParameterizedTest(name = "초기상태 : {0}, 변경상태 : {1}")
        @CsvSource(value = {"COOKING,MEAL", "MEAL,COMPLETION", "COOKING,COMPLETION"})
        @DisplayName("성공 테스트")
        void success(final String originalStatus, final String statusToChange) {
            // given
            testOrder.changeOrderStatus(OrderStatus.valueOf(originalStatus));

            final OrderStatusChangeRequest changeRequest = new OrderStatusChangeRequest(statusToChange);

            // when
            final OrderResponse response = orderService.changeOrderStatus(testOrder.getId(), changeRequest);

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(response.getId()).isEqualTo(testOrder.getId());
                softly.assertThat(response.getOrderStatus()).isEqualTo(statusToChange);
            });
        }

        @ParameterizedTest(name = "초기상태 : {0}, 변경상태 : {1}")
        @CsvSource(value = {"COMPLETION,MEAL", "COMPLETION,COOKING", "COMPLETION,COMPLETION"})
        @DisplayName("실패 테스트")
        void fail(final String originalStatus, final String statusToChange) {
            // given
            testOrder.changeOrderStatus(OrderStatus.valueOf(originalStatus));

            final OrderStatusChangeRequest changeRequest = new OrderStatusChangeRequest(statusToChange);

            // when
            // then
            final Long orderId = testOrder.getId();
            Assertions.assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, changeRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @DisplayName("주문들 리스트 조회 테스트")
    void getOrderList() {
        // given
        final Order order = new Order.OrderFactory(notEmptyTable)
                .addMenu(testMenu, 1L)
                .create();
        final Order savedOrder = orderRepository.save(order);

        // when
        final List<OrderResponse> response = orderService.list();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response).isNotEmpty();
            final OrderResponse savedResult = response.get(response.size() - 1);
            softly.assertThat(savedResult.getId()).isEqualTo(savedOrder.getId());
        });
    }
}
