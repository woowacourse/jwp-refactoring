package kitchenpos.application.order.dto;

import java.math.BigDecimal;
import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemResponse {

    private final Long seq;
    private final String name;
    private final BigDecimal price;
    private final long quantity;

    private OrderLineItemResponse(final Long seq, final String name,
                                  final BigDecimal price, final long quantity) {
        this.seq = seq;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(final OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getName(),
                orderLineItem.getPrice(), orderLineItem.getQuantity());
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
