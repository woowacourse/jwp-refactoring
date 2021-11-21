//package kitchenpos.fixture;
//
//import java.util.Arrays;
//import java.util.List;
//import kitchenpos.domain.Order;
//import kitchenpos.domain.OrderLineItem;
//
//public class OrderFixture {
//
//    public Order 주문_생성(Long orderTableId, String orderStatus, List<OrderLineItem> orderLineItems) {
//        Order order = new Order();
//        order.setOrderTableId(orderTableId);
//        order.setOrderStatus(orderStatus);
//        order.setOrderLineItems(orderLineItems);
//        return order;
//    }
//
//    public Order 주문_생성(Long id, Long orderTableId, String orderStatus,
//            List<OrderLineItem> orderLineItems) {
//        Order order = new Order();
//        order.setId(id);
//        order.setOrderTableId(orderTableId);
//        order.setOrderStatus(orderStatus);
//        order.setOrderLineItems(orderLineItems);
//        return order;
//    }
//
//    public List<Order> 주문_리스트_생성(Order... orders) {
//        return Arrays.asList(orders);
//    }
//}
