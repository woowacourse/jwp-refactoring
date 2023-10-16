package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest extends MockServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    private OrderTable orderTable;
    private Order order;
    private OrderLineItem firstOrderLineItem;
    private OrderLineItem secondOrderLineItem;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setTableGroupId(null);
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(0);

        order = new Order();
        order.setId(1L);
        order.setOrderTableId(orderTable.getId());

        firstOrderLineItem = new OrderLineItem();
        firstOrderLineItem.setSeq(1L);
        firstOrderLineItem.setOrderId(order.getId());
        firstOrderLineItem.setMenuId(1L);

        secondOrderLineItem = new OrderLineItem();
        secondOrderLineItem.setSeq(2L);
        secondOrderLineItem.setOrderId(order.getId());
        secondOrderLineItem.setMenuId(2L);
    }

    @Test
    void 주문_목록을_조회한다() {
        // given
        List<Order> expected = List.of(order);
        order.setOrderLineItems(List.of(firstOrderLineItem, secondOrderLineItem));

        Order mockReturnOrder = new Order();
        mockReturnOrder.setOrderTableId(order.getOrderTableId());
        mockReturnOrder.setId(order.getId());

        BDDMockito.given(orderDao.findAll())
                .willReturn(List.of(mockReturnOrder));

        BDDMockito.given(orderLineItemDao.findAllByOrderId(mockReturnOrder.getId()))
                .willReturn(List.of(firstOrderLineItem, secondOrderLineItem));

        // when
        List<Order> actual = orderService.list();

        // then
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void 주문을_추가한다() {
        // given
        Order expected = new Order();
        expected.setId(1L);
        expected.setOrderTableId(order.getOrderTableId());
        expected.setOrderStatus(OrderStatus.COOKING.name());
        expected.setOrderLineItems(List.of(firstOrderLineItem, secondOrderLineItem));

        Order argumentOrder = new Order();
        argumentOrder.setOrderTableId(order.getOrderTableId());
        argumentOrder.setOrderLineItems(List.of(firstOrderLineItem, secondOrderLineItem));

        BDDMockito.given(menuDao.countByIdIn(BDDMockito.anyList()))
                .willReturn(Long.valueOf(argumentOrder.getOrderLineItems().size()));

        BDDMockito.given(orderTableDao.findById(argumentOrder.getOrderTableId()))
                .willReturn(Optional.of(orderTable));

        Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setOrderStatus(OrderStatus.COOKING.name());
        savedOrder.setOrderTableId(argumentOrder.getOrderTableId());
        BDDMockito.given(orderDao.save(argumentOrder))
                .willReturn(savedOrder);

        BDDMockito.given(orderLineItemDao.save(firstOrderLineItem))
                .willReturn(firstOrderLineItem);
        BDDMockito.given(orderLineItemDao.save(secondOrderLineItem))
                .willReturn(secondOrderLineItem);

        // when
        Order actual = orderService.create(argumentOrder);

        // then
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        BDDMockito.then(orderLineItemDao)
                .should(BDDMockito.times(2))
                .save(BDDMockito.any(OrderLineItem.class));
    }

    @Test
    void 주문을_추가할_때_주문_안에_주문아이템_아이디들_값이_비어있으면_예외를_던진다() {
        // given
        Order order = new Order();
        order.setOrderLineItems(Collections.emptyList());

        // when, then
        Assertions.assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
        BDDMockito.then(menuDao)
                .should(BDDMockito.times(0))
                .countByIdIn(BDDMockito.anyList());
    }

    @Test
    void 주문을_추가할_때_존재하지_않는_주문아이템이_있으면_예외를_던진다() {
        // given
        Order order = new Order();
        order.setOrderLineItems(List.of(firstOrderLineItem));

        BDDMockito.given(menuDao.countByIdIn(BDDMockito.anyList()))
                .willReturn(0L);

        // when, then
        Assertions.assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
        BDDMockito.then(orderTableDao)
                .should(BDDMockito.times(0))
                .findById(BDDMockito.anyLong());
    }

    @Test
    void 주문을_추가할_때_존재하지_않는_주문테이블이면_예외를_던진다() {
        // given
        Order order = new Order();
        order.setOrderTableId(1L);
        order.setOrderLineItems(List.of(firstOrderLineItem));

        BDDMockito.given(menuDao.countByIdIn(BDDMockito.anyList()))
                .willReturn(1L);
        BDDMockito.given(orderTableDao.findById(BDDMockito.anyLong()))
                .willReturn(Optional.empty());

        // when, then
        Assertions.assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_추가할_때_주문테이블이_주문을_할_수_없는_주문테이블이면_예외를_던진다() {
        // given
        Order order = new Order();
        order.setOrderLineItems(List.of(firstOrderLineItem));
        OrderTable orderTable = this.orderTable;
        orderTable.setEmpty(true);
        order.setOrderTableId(orderTable.getId());

        BDDMockito.given(menuDao.countByIdIn(BDDMockito.anyList()))
                .willReturn(1L);
        BDDMockito.given(orderTableDao.findById(BDDMockito.anyLong()))
                .willReturn(Optional.of(orderTable));

        // when, then
        Assertions.assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문상태를_수정한다() {
        // given
        Order expected = new Order();
        expected.setId(1L);
        expected.setOrderStatus(OrderStatus.MEAL.name());
        expected.setOrderLineItems(List.of(firstOrderLineItem, secondOrderLineItem));

        Long argumentOrderId = 1L;
        Order argumentOrder = new Order();
        argumentOrder.setOrderStatus(OrderStatus.MEAL.name());

        Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setOrderStatus(OrderStatus.COOKING.name());
        BDDMockito.given(orderDao.findById(argumentOrderId))
                .willReturn(Optional.of(savedOrder));

        BDDMockito.given(orderLineItemDao.findAllByOrderId(argumentOrderId))
                .willReturn(List.of(firstOrderLineItem, secondOrderLineItem));

        // when
        Order actual = orderService.changeOrderStatus(argumentOrderId, argumentOrder);

        // then
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void 주문상태를_수정할_때_주문_아이디에_해당하는_주문이_없으면_예외를_던진다() {
        // given
        Long orderId = 1L;
        Order order = new Order();
        order.setOrderStatus(OrderStatus.MEAL.name());

        BDDMockito.given(orderDao.findById(orderId))
                .willReturn(Optional.empty());

        // when, then
        Assertions.assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문상태를_수정할_때_수정하려는_주문의_상태가_COMPLETION_이면_예외를_던진다() {
        // given
        Long orderId = 1L;
        Order order = new Order();
        order.setOrderStatus(OrderStatus.MEAL.name());

        Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setOrderStatus(OrderStatus.COMPLETION.name());
        BDDMockito.given(orderDao.findById(orderId))
                .willReturn(Optional.of(savedOrder));

        // when, then
        Assertions.assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문상태를_수정할_때_수정하려는_주문의_상태가_존재하지_않는_주문상태이면_예외를_던진다() {
        // given
        Long orderId = 1L;
        Order order = new Order();
        order.setOrderStatus("NoSuchStatus");

        Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setOrderStatus(OrderStatus.MEAL.name());
        BDDMockito.given(orderDao.findById(orderId))
                .willReturn(Optional.of(savedOrder));

        // when, then
        Assertions.assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, order))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
