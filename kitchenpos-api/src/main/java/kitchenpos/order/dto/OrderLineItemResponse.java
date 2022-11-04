package kitchenpos.order.dto;

import java.math.BigDecimal;
import kitchenpos.order.OrderLineItem;

public class OrderLineItemResponse {

    private Long seq;
    private String name;
    private BigDecimal price;
    private long quantity;

    private OrderLineItemResponse() {
    }

    public OrderLineItemResponse(final OrderLineItem orderLineItem) {
        this.seq = orderLineItem.getSeq();
        this.name = orderLineItem.getName();
        this.price = orderLineItem.getPrice().getValue();
        this.quantity = orderLineItem.getQuantity();
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
