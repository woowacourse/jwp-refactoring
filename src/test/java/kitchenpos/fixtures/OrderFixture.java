package kitchenpos.fixtures;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.Arrays;

import static kitchenpos.fixtures.OderLineItemFixture.첫번째주문의아이템1;
import static kitchenpos.fixtures.OderLineItemFixture.첫번째주문의아이템2;

public class OrderFixture {

    public static Order 첫번째주문() {
        Order order = new Order();
        order.setOrderTableId(1L);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(Arrays.asList(첫번째주문의아이템1(), 첫번째주문의아이템2()));
        return order;
    }

    public static Order 첫번째주문등록(OrderStatus orderStatus) {
        Order order = 첫번째주문();
        order.setOrderStatus(orderStatus.name());
        return order;
    }

    public static Order 첫번째주문등록() {
        Order order = 첫번째주문();
        order.setId(1L);
        return order;
    }
}
