package kitchenpos.order.dto;

import java.math.BigDecimal;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderedMenu;

public class OrderLineItemResponse {

    private Long seq;
    private String name;
    private BigDecimal price;
    private Long quantity;

    public OrderLineItemResponse() {
    }

    public OrderLineItemResponse(Long seq, OrderedMenu orderedMenu, Long quantity) {
        this.seq = seq;
        this.name = orderedMenu.getName();
        this.price = orderedMenu.getPrice();
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getQuantity() {
        return quantity;
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
            orderLineItem.getSeq(),
            orderLineItem.getOrderedMenu(),
            orderLineItem.getQuantity()
        );
    }
}
