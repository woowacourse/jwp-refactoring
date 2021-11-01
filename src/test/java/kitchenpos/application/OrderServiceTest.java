package kitchenpos.application;

import kitchenpos.ServiceTest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.fixture.OrderFixture.createOrder;
import static kitchenpos.fixture.OrderTableFixture.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ServiceTest
class OrderServiceTest {

    @Mock
    private MenuDao mockMenuDao;

    @Mock
    private OrderDao mockOrderDao;

    @Mock
    private OrderLineItemDao mockOrderLineItemDao;

    @Mock
    private OrderTableDao mockOrderTableDao;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("주문 생성")
    @Nested
    class createOrder {

        @BeforeEach
        void setUp() {
            when(mockOrderDao.save(any())).then(AdditionalAnswers.returnsFirstArg());
            when(mockOrderLineItemDao.save(any())).then(AdditionalAnswers.returnsFirstArg());
            when(mockOrderTableDao.findById(any())).thenReturn(Optional.of(createOrderTable()));
        }

        @DisplayName("주문을 생성한다.")
        @Test
        void create() {
            Order order = createOrder();
            when(mockMenuDao.countByIdIn(any())).thenReturn((long) order.getOrderLineItems().size());
            Order savedOrder = orderService.create(order);
            assertThat(savedOrder).isEqualTo(order);
        }

        @DisplayName("주문 항목이 1개 이상이어야한다.")
        @Test
        void createWithInvalidOrderItemList() {
            Order order = createOrder(Collections.emptyList());
            when(mockMenuDao.countByIdIn(any())).thenReturn((long) order.getOrderLineItems().size());
            assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 항목의 메뉴가 존재해야 한다.")
        @Test
        void createWithNonexistentMenu() {
            when(mockMenuDao.countByIdIn(any())).thenReturn(0L);
            assertThatThrownBy(() -> orderService.create(createOrder())).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블은 비어있지 않아야한다.")
        @Test
        void createWithEmptyTable() {
            when(mockOrderTableDao.findById(any())).thenReturn(Optional.of(createOrderTable(true)));
            assertThatThrownBy(() -> orderService.create(createOrder())).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("주문 목록을 반환한다.")
    @Test
    void list() {
        Order order = createOrder();
        when(mockOrderDao.findAll()).thenReturn(Collections.singletonList(order));
        when(mockOrderLineItemDao.findAllByOrderId(any())).thenReturn(order.getOrderLineItems());
        List<Order> list = orderService.list();
        assertAll(
                () -> assertThat(list).hasSize(1),
                () -> assertThat(list).contains(order)
        );
    }

    @DisplayName("주문 상태 변경")
    @Nested
    class ChangeOrderStatus {

        @Captor
        private ArgumentCaptor<Order> argumentCaptor;
        private Order savedOrder;

        @BeforeEach
        void setUp() {
            savedOrder = createOrder();
            when(mockOrderDao.findById(savedOrder.getId())).thenReturn(Optional.of(savedOrder));
        }

        @DisplayName("주문의 상태를 변경한다.")
        @Test
        void changeOrderStatus() {
            String newStatus = OrderStatus.COOKING.name();
            Order updateOrder = createOrder(savedOrder.getId(), newStatus);
            orderService.changeOrderStatus(savedOrder.getId(), updateOrder);

            verify(mockOrderDao).save(argumentCaptor.capture());
            assertThat(argumentCaptor.getValue().getOrderStatus()).isEqualTo(newStatus);
        }

        @DisplayName("COOKING, MEAL, COMPLETION이 아닌 다른 상태로 주문을 변경할 수 없다.")
        @Test
        void changeOrderStatusWithInvalidStatus() {
            Order updateOrder = createOrder(savedOrder.getId(), "INVALID_STATUS");
            assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), updateOrder))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 완료 상태의 주문은 상태를 변경할 수 없다.")
        @Test
        void changeOrderStatusInCompletion() {
            Order savedOrder = createOrder(OrderStatus.COMPLETION.name());
            when(mockOrderDao.findById(savedOrder.getId())).thenReturn(Optional.of(savedOrder));

            Order updateOrder = createOrder(savedOrder.getId(), OrderStatus.COOKING.name());
            assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), updateOrder))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
