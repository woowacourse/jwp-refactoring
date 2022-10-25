package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableService tableService;

    private Long orderTableId;

    @BeforeEach
    void init(){
        orderTableId = tableService.create(new OrderTable()).getId();
    }

    @Test
    @DisplayName("존재하지 않는 menu가 orderItems에 있을 경우 예외를 발생시킨다.")
    void createWithNotExistOrderItemError() {
        //when
        Order order = new Order(orderTableId, "COOKING", LocalDateTime.now(),
            generateOrderLineItemAsList(99L, 1));

        //then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 orderTable이 있을 경우 예외를 발생시킨다.")
    void createWithNotExistOrderTableError() {
        //when
        Order order = new Order(99L, "COOKING", LocalDateTime.now(),
            generateOrderLineItemAsList(1L, 1));

        //then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("orderTable이 비어있을 경우 예외를 발생시킨다.")
    void createWithEmptyOrderTableError() {
        //given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        long emptyOrderTableId = tableService.create(orderTable).getId();

        //when
        Order order = new Order(emptyOrderTableId, "COOKING", LocalDateTime.now(),
            generateOrderLineItemAsList(1L, 1));

        //then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("전체 주문 목록을 조회한다.")
    void findList() {
        //given
        List<Order> orders = orderService.list();

        //when
        Order order = new Order(orderTableId, "COOKING", LocalDateTime.now(),
            generateOrderLineItemAsList(1L, 1));
        orderService.create(order);

        //then
        assertThat(orderService.list()).hasSize(orders.size() + 1);
    }

    @Test
    @DisplayName("주문 상태를 변경한다.")
    void changeOrderStatus() {
        //given
        Order order = new Order(orderTableId, "COOKING", LocalDateTime.now(),
            generateOrderLineItemAsList(1L, 1));
        Long orderId = orderService.create(order).getId();

        //when
        Order changeOrder = new Order(orderTableId, "MEAL", LocalDateTime.now(),
            generateOrderLineItemAsList(1L, 1));
        Order actual = orderService.changeOrderStatus(orderId, changeOrder);

        //then
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
        //given
        Order order = new Order(orderTableId, "COOKING", LocalDateTime.now(),
            generateOrderLineItemAsList(1L, 1));

        //then
        assertThatThrownBy(() -> orderService.changeOrderStatus(99999L, order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("COMPLETION 상태의 주문일 경우 예외를 발생시킨다.")
    void changeOrderStatusCompletionError() {
        //given
        Order order = new Order(orderTableId, "COOKING", LocalDateTime.now(),
            generateOrderLineItemAsList(1L, 1));
        Long orderId = orderService.create(order).getId();

        //when
        Order changeOrder = new Order(orderTableId, "COMPLETION", LocalDateTime.now(),
            generateOrderLineItemAsList(1L, 1));
        orderService.changeOrderStatus(orderId, changeOrder);

        //then
        assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, changeOrder))
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
