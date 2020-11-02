package kitchenpos.application;

import static kitchenpos.fixture.OrderFixture.*;
import static kitchenpos.fixture.OrderLineItemFixture.*;
import static kitchenpos.fixture.OrderTableFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.TestObjectUtils;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

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

    @DisplayName("1 개 이상의 등록된 메뉴로 주문을 등록할 수 있다.")
    @Test
    void createTest() {
        OrderLineItem orderLineItem = TestObjectUtils.createOrderLineItem(1L, null, 1L, 1);
        Order createOrder = TestObjectUtils.createOrder(null, 5L, null, null,
                Collections.singletonList(orderLineItem));

        when(menuDao.countByIdIn(anyList())).thenReturn(1L);
        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(ORDER_TABLE5));
        when(orderLineItemDao.save(any())).thenReturn(ORDER_LINE_ITEM1);
        when(orderDao.save(any())).thenReturn(ORDER1);

        Order savedOrder = orderService.create(createOrder);
        assertAll(
                () -> assertThat(savedOrder.getId()).isEqualTo(1L),
                () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(savedOrder.getOrderTableId()).isEqualTo(5L),
                () -> assertThat(savedOrder.getOrderLineItems().get(0).getSeq()).isEqualTo(1L),
                () -> assertThat(savedOrder.getOrderLineItems().get(0).getOrderId()).isEqualTo(1L),
                () -> assertThat(savedOrder.getOrderLineItems().get(0).getMenuId()).isEqualTo(1L),
                () -> assertThat(savedOrder.getOrderLineItems().get(0).getQuantity()).isEqualTo(1L)
        );
    }

    @DisplayName("빈 테이블에는 주문을 등록할 수 없다")
    @Test
    void notCreateTest_when_emptyTable() {
        OrderLineItem orderLineItem = TestObjectUtils.createOrderLineItem(1L, null, 1L, 1);
        Order createOrder = TestObjectUtils.createOrder(null, 1L, null, null,
                Collections.singletonList(orderLineItem));

        when(menuDao.countByIdIn(anyList())).thenReturn(1L);
        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(ORDER_TABLE1));

        assertThatThrownBy(() -> orderService.create(createOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 목록을 조회할 수 있다.")
    @Test
    void listTest() {
        when(orderDao.findAll()).thenReturn(ORDERS);

        assertAll(
                () -> assertThat(orderService.list().size()).isEqualTo(1),
                () -> assertThat(orderService.list().get(0).getId()).isEqualTo(1L)
        );
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatusTest() {
        when(orderDao.findById(any())).thenReturn(Optional.of(ORDER3));
        when(orderLineItemDao.findAllByOrderId(any())).thenReturn(ORDER_LINE_ITEMS3);

        Order savedOrder = orderService.changeOrderStatus(3L, CHANGING_MEAL_ORDER);

        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문 상태가 계산 완료인 경우 변경할 수 없다.")
    @Test
    void notChangeOrderStatusTest_when_OrderStatusIsComplication() {
        when(orderDao.findById(any())).thenReturn(Optional.of(ORDER2));

        assertThatThrownBy(() -> orderService.changeOrderStatus(2L, CHANGING_MEAL_ORDER))
                .isInstanceOf(IllegalArgumentException.class);
    }
}