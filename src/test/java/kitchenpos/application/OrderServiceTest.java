package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.utils.TestFixture;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private MenuDao menuDao;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderLineItemDao orderLineItemDao;
    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("create: 주문 등록 테스트")
    @Test
    void createTest() {
        final OrderLineItem orderLineItem = TestFixture.getOrderLineItem();
        final Order order = TestFixture.getOrderWithCooking(orderLineItem, 1L);
        final OrderTable orderTable = TestFixture.getOrderTableWithNotEmpty();

        when(menuDao.countByIdIn(anyList())).thenReturn(1L);
        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));
        when(orderDao.save(any())).thenReturn(order);
        when(orderLineItemDao.save(any())).thenReturn(orderLineItem);

        final Order actual = orderService.create(order);

        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getOrderLineItems()).contains(orderLineItem)
        );
    }

    @DisplayName("create: 주문 등록 시 테이블이 비어있는 경우 예외처리")
    @Test
    void createTestByOrderTableEmpty() {
        final OrderLineItem orderLineItem = TestFixture.getOrderLineItem();
        final Order order = TestFixture.getOrderWithCooking(orderLineItem, 1L);
        final OrderTable orderTable = TestFixture.getOrderTableWithEmpty();

        when(menuDao.countByIdIn(anyList())).thenReturn(1L);
        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("list: 전체 주문을 확인하는 테스트")
    @Test
    void listTest() {
        final OrderLineItem orderLineItem = TestFixture.getOrderLineItem();
        final Order order = TestFixture.getOrderWithCooking(orderLineItem, 1L);
        when(orderDao.findAll()).thenReturn(Collections.singletonList(order));

        final List<Order> orders = orderService.list();

        assertAll(
                () -> assertThat(orders).hasSize(1),
                () -> assertThat(orders.get(0)).isEqualTo(order)
        );
    }

    @DisplayName("changeOrderStatus: 주문 상태를 변경하는 테스트")
    @Test
    void changeOrderStatusTest() {
        final OrderLineItem orderLineItem = TestFixture.getOrderLineItem();
        final Order orderWithCooking = TestFixture.getOrderWithCooking(orderLineItem, 1L);
        final Order orderWithCompletion = TestFixture.getOrderWithCompletion(orderLineItem);

        when(orderDao.findById(anyLong())).thenReturn(Optional.of(orderWithCooking));
        when(orderDao.save(any())).thenReturn(orderWithCompletion);

        final Order order = orderService.changeOrderStatus(1L, orderWithCompletion);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @DisplayName("changeOrderStatus: 주문 상태가 Completion일 경우 예외처리")
    @Test
    void changeOrderStatusTestByCompletionOfOrderState() {
        final OrderLineItem orderLineItem = TestFixture.getOrderLineItem();
        final Order orderWithCooking = TestFixture.getOrderWithCompletion(orderLineItem);
        final Order orderWithCompletion = TestFixture.getOrderWithCompletion(orderLineItem);

        when(orderDao.findById(anyLong())).thenReturn(Optional.of(orderWithCooking));

        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, orderWithCompletion))
                .isInstanceOf(IllegalArgumentException.class);

    }
}
