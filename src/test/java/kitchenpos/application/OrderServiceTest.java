package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableService tableService;

    @Test
    @DisplayName("orderItem이 비어있으면 예외를 발생시킨다.")
    void createWithEmptyOrderItemError() {
        //when
        Order order = new Order();

        //then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 menu가 orderItems에 있을 경우 예외를 발생시킨다.")
    void createWithNotExistOrderItemError() {
        //when
        Order order = new Order();
        order.setOrderLineItems(generateOrderLineItemAsList(999L, 1));

        //then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 orderTable이 있을 경우 예외를 발생시킨다.")
    void createWithNotExistOrderTableError() {
        //when
        Order order = new Order();
        order.setOrderLineItems(generateOrderLineItemAsList(1L, 1));

        //then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("orderTable이 비어있을 경우 예외를 발생시킨다.")
    void createWithEmptyOrderTableError() {
        //when
        Order order = new Order();
        order.setOrderLineItems(generateOrderLineItemAsList(1L, 1));
        order.setOrderTableId(1L);

        //then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("전체 주문 목록을 조회한다.")
    void findList() {
        //given
        Order order = new Order();
        order.setOrderTableId(tableService.create(new OrderTable()).getId());
        order.setOrderLineItems(generateOrderLineItemAsList(1L, 1));
        orderService.create(order);
        orderService.create(order);

        List<Order> orders = orderService.list();

        assertThat(orders).hasSizeGreaterThan(1);
    }

    @Test
    @DisplayName("주문 상태를 변경한다.")
    void changeOrderStatus() {
        Long orderTableId = tableService.create(new OrderTable()).getId();
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(generateOrderLineItemAsList(1L, 1));
        Long orderId = orderService.create(order).getId();

        Order orderDto = new Order();
        orderDto.setOrderStatus(OrderStatus.MEAL.name());
        Order actual = orderService.changeOrderStatus(orderId, orderDto);
        assertAll(
            () -> assertThat(actual.getOrderTableId()).isEqualTo(orderTableId),
            () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name()),
            () -> assertThat(actual.getOrderedTime()).isNotNull(),
            () -> assertThat(actual.getOrderLineItems()).hasSize(1)
        );
    }

    @Test
    @DisplayName("존재하지 않는 주문일 경우 예외를 발생시킨다.")
    void changeOrderStatusNotExistOrderError() {
        assertThatThrownBy(() -> orderService.changeOrderStatus(99999L, new Order()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("COMPLETION 상태의 주문일 경우 예외를 발생시킨다.")
    void changeOrderStatusCompletionError() {
        Order order = new Order();
        order.setOrderTableId(tableService.create(new OrderTable()).getId());
        order.setOrderLineItems(generateOrderLineItemAsList(1L, 1));
        Long orderId = orderService.create(order).getId();

        Order orderDto = new Order();
        orderDto.setOrderStatus(OrderStatus.COMPLETION.name());
        orderService.changeOrderStatus(orderId, orderDto);

        assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, orderDto))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private ArrayList<OrderLineItem> generateOrderLineItemAsList(Long menuId, int quantity) {
        ArrayList<OrderLineItem> orderLineItems = new ArrayList<>();

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);

        orderLineItems.add(orderLineItem);
        return orderLineItems;
    }

}
