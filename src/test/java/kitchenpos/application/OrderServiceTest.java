package kitchenpos.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends KitchenPosServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    void create() {
//        OrderLineItem orderLineItem = new OrderLineItem();
//        orderLineItem.setMenuId(getCreatedMenuId());
//        orderLineItem.setQuantity();
//
//        Order order = new Order();
//        order.setOrderTableId(getCreatedEmptyOrderTableId());
//        order.setOrderLineItems(null);
//        orderService.create(order);
    }

    @Test
    void list() {
    }

    @Test
    void changeOrderStatus() {
    }
}
