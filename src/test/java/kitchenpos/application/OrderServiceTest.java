package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private List<Order> standardOrders;
    private List<OrderLineItem> standardOrderLineItems;
    private Order standardOrder;
    private OrderTable standardOrderTable;

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

    @BeforeEach
    void setUp() {
        standardOrder = new Order();
        standardOrder.setId(1L);
        standardOrder.setOrderTableId(1L);
        standardOrder.setOrderStatus(OrderStatus.COOKING.name());
        standardOrder.setOrderedTime(LocalDateTime.now());

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(1L);
        orderLineItem.setOrderId(1L);
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1L);
        standardOrderLineItems = new LinkedList<>();
        standardOrderLineItems.add(orderLineItem);

        standardOrders = new LinkedList<>();
        standardOrders.add(standardOrder);
        standardOrder.setOrderLineItems(standardOrderLineItems);

        standardOrderTable = new OrderTable();
        standardOrderTable.setId(1L);
        standardOrderTable.setTableGroupId(1L);
    }

    @DisplayName("주문들을 조회한다.")
    @Test
    void getOrders() {
        //given
        given(orderDao.findAll()).willReturn(standardOrders);
        given(orderLineItemDao.findAllByOrderId(standardOrder.getId()))
            .willReturn(standardOrderLineItems);

        //when
        List<Order> orders = orderService.list();

        //then
        assertThat(orders.size()).isEqualTo(1);
    }

    @DisplayName("주문이 비어서 들어선 안 된다.")
    @Test
    void createOrderOfNull() {
        //given
        standardOrder.setOrderLineItems(null);

        //when

        //then
        assertThatThrownBy(() -> orderService.create(standardOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 리스트가 메뉴와 일치해선 안 된다.")
    @Test
    void createOrderWithExistedItems() {
        //given
        given(menuDao.countByIdIn(Arrays.asList(1L))).willReturn(1L);

        //when

        //then
        assertThatThrownBy(() -> orderService.create(standardOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어선 안 된다.")
    @Test
    void createOrderWithOrderTable() {
        //given
        given(menuDao.countByIdIn(any())).willReturn(1L);
        given(orderTableDao.findById(1L)).willReturn(Optional.empty());

        //when

        //then
        assertThatThrownBy(() -> orderService.create(standardOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 추가한다.")
    @Test
    void createOrder() {
        //given
        given(menuDao.countByIdIn(any())).willReturn(1L);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(standardOrderTable));
        given(orderDao.save(any())).willReturn(standardOrder);

        //when
        Order order = orderService.create(standardOrder);

        //then
        assertAll(
            () -> assertThat(order.getOrderTableId()).isEqualTo(1L),
            () -> assertThat(order.getOrderLineItems().size()).isEqualTo(1),
            () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name())
        );
    }

    @DisplayName("주문 상태 변경 시 주문 번호가 등록이 되어 있어야 한다.")
    @Test
    void changeNullOrderStatus() {
        //given
        given(orderDao.findById(1L)).willReturn(Optional.empty());

        //when

        //then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, standardOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태 시 완료된 주문은 아니어야 한다.")
    @Test
    void changeCompletedOrderStatus() {
        //given
        standardOrder.setOrderStatus(OrderStatus.COMPLETION.name());
        given(orderDao.findById(1L)).willReturn(Optional.of(standardOrder));

        //when

        //then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, standardOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        //given
        standardOrder.setOrderStatus(OrderStatus.MEAL.name());
        given(orderDao.findById(1L)).willReturn(Optional.of(standardOrder));
        given(orderLineItemDao.findAllByOrderId(1L)).willReturn(standardOrderLineItems);

        //when
        Order order = orderService.changeOrderStatus(1L, standardOrder);

        //then
        assertAll(
            () -> assertThat(order.getId()).isEqualTo(1L),
            () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name()),
            () -> assertThat(order.getOrderLineItems().size()).isEqualTo(1),
            () -> assertThat(order.getOrderTableId()).isEqualTo(1)
        );
    }
}