package kitchenpos.application;

import kitchenpos.Order.domain.Order;
import kitchenpos.Order.domain.OrderLineItem;
import kitchenpos.Order.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class OrderServiceTest extends ServiceTest {

    @Test
    @DisplayName("Order 요청 시에는 order_line_item이 반드시 있어야 한다.(메뉴 주문을 무조건 해야 한다)")
    public void orderLineItemEmptyException() {
        //given & when
        Order order = new Order(orderTable1.getId(), Collections.emptyList());

        //then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("동일한 Menu가 별개의 order_line_item에 들어있어서는 안된다.(메뉴의 종류의 개수와 order_line_item의 개수가 같아야 한다)")
    public void notDistinctOrderLineItemsException() {
        //given & when
        Order order = new Order(orderTable1.getId(), Arrays.asList(orderLineItem(), orderLineItem()));

        //then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("비어있는 order_table에서 order 요청을 할 수 없다.")
    public void emptyOrderTableOrderException() {
        //given & when
        Order order = new Order(orderTable3.getId(), Collections.singletonList(orderLineItem()));

        //then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Order를 등록할 수 있다.")
    public void enrollOrder() {
        //given
        Order order = createOrder();

        //when * then
        assertDoesNotThrow(() -> orderService.create(order));
    }

    @Test
    @DisplayName("존재하는 Order 조회를 할 수 있다.")
    public void findAll() {
        //given
        Order order = createOrder();

        //when
        orderService.create(order);
        List<Order> orders = orderService.list();

        //then
        assertThat(orders).hasSize(2);
        for (Order o : orders) {
            assertThat(o.getOrderLineItems()).isNotEmpty();
        }
    }

    @Test
    @DisplayName("존재하지 않는 Order의 상태를 바꿀 수 없다.")
    public void changeNotExistOrderStatusException() {
        assertThatThrownBy(() -> orderService.changeOrderStatus(10L, OrderStatus.COMPLETION.name()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Order의 상태를 바꿀 수 있다.")
    public void changeOrderStatus() {
        //given
        Order order = createOrder();

        //when
        Order savedOrder = orderService.create(order);

        //then
        assertDoesNotThrow(() -> orderService.changeOrderStatus(savedOrder.getId(), OrderStatus.MEAL.name()));
        Order statusChangedOrder = orderService.changeOrderStatus(savedOrder.getId(), OrderStatus.MEAL.name());
        assertThat(statusChangedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    @DisplayName("상태 변경 후에는 order_line_item을 포함한 Order를 반환받는다.")
    public void receiveOrderAfterChangeOrderStatus() {
        //given
        Order order = createOrder();

        //when
        Order savedOrder = orderService.create(order);
        Order statusChangedOrder = orderService.changeOrderStatus(savedOrder.getId(), OrderStatus.MEAL.name());

        //then
        assertThat(statusChangedOrder.getId()).isEqualTo(savedOrder.getId());
        assertThat(statusChangedOrder.getOrderLineItems()).isNotEmpty();
    }

    @Test
    @DisplayName("이미 COMPLETION 상태의 Order는 상태를 변경할 수 없다.")
    public void changeCompletionStatusOrderException() {
        //given
        Order order = createOrder();

        //when
        Order savedOrder = orderService.create(order);
        orderService.changeOrderStatus(savedOrder.getId(), OrderStatus.MEAL.name());
        orderService.changeOrderStatus(savedOrder.getId(), OrderStatus.COMPLETION.name());

        //then
        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), OrderStatus.COMPLETION.name()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Order createOrder() {
        return new Order(orderTable2.getId(), Collections.singletonList(orderLineItem()));
    }

    private OrderLineItem orderLineItem() {
        return new OrderLineItem(menu.getId(), 1L);
    }
}