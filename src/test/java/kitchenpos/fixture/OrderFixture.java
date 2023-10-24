package kitchenpos.fixture;

import java.util.List;
import java.util.function.Consumer;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.aspectj.weaver.ast.Or;

public enum OrderFixture {

    ORDER_1(1L, 1L, "COOKING", null, List.of(OrderLineItemFixture.ORDER_LINE_ITEM_1.toEntity())),
    ;

    private final Long id;
    private final Long orderTableId;
    private final String orderStatus;
    private final String orderedTime;
    private final List<OrderLineItem> orderLineItems;

    OrderFixture(Long id, Long orderTableId, String orderStatus, String orderedTime,
        List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static Order computeDefaultOrder(Consumer<Order> consumer) {
        Order order = new Order();
        order.setId(1L);
        order.setOrderTableId(1L);
        order.setOrderStatus("COOKING");
        order.setOrderedTime(null);
        order.setOrderLineItems(List.of(OrderLineItemFixture.ORDER_LINE_ITEM_1.toEntity()));
        consumer.accept(order);
        return order;
    }

    public Order toEntity() {
        Order order = new Order();
        order.setId(id);
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(null);
        order.setOrderLineItems(orderLineItems);
        return order;
    }

    private enum OrderLineItemFixture {

        ORDER_LINE_ITEM_1(1L, 1L, 1L, 1L),
        ;

        private final Long seq;
        private final Long orderId;
        private final Long menuId;
        private final Long quantity;

        OrderLineItemFixture(Long seq, Long orderId, Long menuId, Long quantity) {
            this.seq = seq;
            this.orderId = orderId;
            this.menuId = menuId;
            this.quantity = quantity;
        }

        private OrderLineItem toEntity() {
            OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setSeq(seq);
            orderLineItem.setOrderId(orderId);
            orderLineItem.setMenuId(menuId);
            orderLineItem.setQuantity(quantity);
            return orderLineItem;
        }
    }
}
