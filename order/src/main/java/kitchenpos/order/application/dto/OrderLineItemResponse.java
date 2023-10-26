package kitchenpos.order.application.dto;

import java.math.BigDecimal;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {

    private Long seq;
    private String name;
    private BigDecimal price;
    private long quantity;

    public OrderLineItemResponse() {
    }

    public OrderLineItemResponse(Long seq, String name, BigDecimal price, long quantity) {
        this.seq = seq;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
                orderLineItem.getSeq(),
                orderLineItem.getName(),
                orderLineItem.getPrice().getPrice(),
                orderLineItem.getQuantity()
        );
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

    public long getQuantity() {
        return quantity;
    }
}
