package kitchenpos.factory;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderLineItemResponse;

public class OrderLineItemFactory {

    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    private OrderLineItemFactory() {

    }

    private OrderLineItemFactory(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemFactory builder() {
        return new OrderLineItemFactory();
    }

    public static OrderLineItemFactory copy(OrderLineItem orderLineItem) {
        return new OrderLineItemFactory(
            orderLineItem.getSeq(),
            orderLineItem.getOrderId(),
            orderLineItem.getMenuId(),
            orderLineItem.getQuantity()
        );
    }

    public static OrderLineItemRequest dto(OrderLineItem orderLineItem) {
        return new OrderLineItemRequest(
            orderLineItem.getSeq(),
            orderLineItem.getOrderId(),
            orderLineItem.getMenuId(),
            orderLineItem.getQuantity()
        );
    }

    public static List<OrderLineItemRequest> dtoList(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
            .map(OrderLineItemFactory::dto)
            .collect(Collectors.toList());
    }

    public OrderLineItemFactory seq(Long seq) {
        this.seq = seq;
        return this;
    }

    public OrderLineItemFactory orderId(Long orderId) {
        this.orderId = orderId;
        return this;
    }

    public OrderLineItemFactory menuId(Long menuId) {
        this.menuId = menuId;
        return this;
    }

    public OrderLineItemFactory quantity(long quantity) {
        this.quantity = quantity;
        return this;
    }

    public OrderLineItem build() {
        return new OrderLineItem(seq, orderId, menuId, quantity);
    }
}
