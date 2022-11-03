package kitchenpos.application.dto;

import java.math.BigDecimal;
import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemResponse {

    private Long seq;
    private Long orderId;
    private String name;
    private BigDecimal price;
    private long quantity;

    protected OrderLineItemResponse() {
    }

    public OrderLineItemResponse(final Long seq, final Long orderId, final String name, final BigDecimal price, final long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse createResponse(final OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
            orderLineItem.getSeq(),
            orderLineItem.getOrderId(),
            orderLineItem.getName(),
            orderLineItem.getPrice(),
            orderLineItem.getQuantity());
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
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
