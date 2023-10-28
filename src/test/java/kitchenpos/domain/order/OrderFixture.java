package kitchenpos.domain.order;

public class OrderFixture {

    public static Order order(Long orderTableId, final OrderStatus orderStatus, final OrderLineItems orderLineItems) {
        return new Order(orderTableId, orderStatus, orderLineItems);
    }
}
