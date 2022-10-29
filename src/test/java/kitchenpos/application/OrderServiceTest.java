package kitchenpos.application;

import kitchenpos.application.request.order.ChangeOrderStatusRequest;
import kitchenpos.application.request.order.OrderLineItemRequest;
import kitchenpos.application.request.order.OrderRequest;
import kitchenpos.application.response.order.OrderResponse;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ServiceTest
class OrderServiceTest {

    private final OrderService orderService;
    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableDao orderTableDao;

    @Autowired
    public OrderServiceTest(OrderService orderService, MenuDao menuDao, OrderDao orderDao,
                            OrderLineItemDao orderLineItemDao, OrderTableDao orderTableDao) {
        this.orderService = orderService;
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @ServiceTest
    class CreateTest {

        private Menu menu;
        private OrderTable orderTable;

        @BeforeEach
        void setUp() {
            this.menu = menuDao.save(new Menu("자장면", 5000, 1));
            this.orderTable = orderTableDao.save(new OrderTable(1L, 1, false));
        }

        @DisplayName("주문을 한다")
        @Test
        void create() {
            final var orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 1);
            final var request = new OrderRequest(orderTable.getId(), orderLineItemRequest);

            final var actual = orderService.create(request);
            assertThat(actual.getId()).isPositive();
        }

        @DisplayName("하나 이상의 주문 항목을 포함해야 한다")
        @Test
        void createWithEmptyOrderLineItems() {
            final var request = new OrderRequest(1L);

            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 항목이 비어 있습니다.");
        }

        @DisplayName("주문 항목의 메뉴는 서로 달라야 한다")
        @Test
        void createWithDuplicatedMenu() {
            final var request = new OrderRequest(1L, List.of(
                    new OrderLineItemRequest(menu.getId(), 1),
                    new OrderLineItemRequest(menu.getId(), 10)));

            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("중복된 메뉴의 주문 항목이 존재합니다.");
        }

        @DisplayName("주문하고자 하는 주문 테이블은 공석이 아니어야 한다")
        @Test
        void createWithEmptyOrderTable() {
            final var emptyOrderTable = orderTableDao.save(new OrderTable(1L, 1, true));

            final var menu = menuDao.save(new Menu("자장면", 5000, 1));
            final var OrderLineItemRequest = new OrderLineItemRequest(menu.getId(), 1);
            final var request = new OrderRequest(emptyOrderTable.getId(), OrderLineItemRequest);

            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 테이블이 비어 있습니다.");
        }

        @DisplayName("존재하는 주문 테이블이어야 주문을 할 수 있다")
        @Test
        void createWithNonExistOrderTable() {
            final var nonExistOrderTableId = 0L;

            final var menu = menuDao.save(new Menu("자장면", 5000, 1));
            final var OrderLineItemRequest = new OrderLineItemRequest(menu.getId(), 1);
            final var request = new OrderRequest(nonExistOrderTableId, OrderLineItemRequest);

            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 테이블을 찾을 수 없습니다.");
        }
    }

    @Nested
    @ServiceTest
    class ChangeOrderStatusTest {

        @DisplayName("주문 상태를 변경한다")
        @Test
        void changeOrderStatus() {
            final var savedOrder = saveOrder(1L, OrderStatus.MEAL, LocalDateTime.now());

            final var request = new ChangeOrderStatusRequest(OrderStatus.COOKING.name());

            final var actual = orderService.changeOrderStatus(savedOrder.getId(), request);

            assertThat(actual.getOrderStatus()).isEqualTo(request.getOrderStatus());
        }

        @DisplayName("결제가 완료되지 않은 주문이어야 한다")
        @Test
        void changeOrderStatusWithCompletedOrder() {
            final var completedOrderStatus = OrderStatus.COMPLETION;

            final var savedOrder = saveOrder(1L, completedOrderStatus, LocalDateTime.now());

            final var request = new ChangeOrderStatusRequest(OrderStatus.COOKING.name());

            final var orderId = savedOrder.getId();
            assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("이미 결제 완료된 주문입니다.");
        }

        @DisplayName("존재하는 주문이어야 한다")
        @Test
        void changeOrderStatusWithNonExistOrder() {
            final var nonExistOrderId = 0L;

            final var request = new ChangeOrderStatusRequest(OrderStatus.COOKING.name());

            assertThatThrownBy(() -> orderService.changeOrderStatus(nonExistOrderId, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문이 존재하지 않습니다.");
        }
    }

    @DisplayName("주문 테이블을 전체 조회한다")
    @Test
    void list() {
        saveOrder(1, OrderStatus.MEAL, LocalDateTime.now());
        saveOrder(2, OrderStatus.COOKING, LocalDateTime.now());
        final List<OrderResponse> actual = orderService.list();

        assertThat(actual).hasSize(2);
    }

    private Order saveOrder(final long orderTableId, final OrderStatus orderStatus, final LocalDateTime orderedTime) {
        final var order = new Order(orderTableId, orderStatus.name(), orderedTime);
        return orderDao.save(order);
    }
}
