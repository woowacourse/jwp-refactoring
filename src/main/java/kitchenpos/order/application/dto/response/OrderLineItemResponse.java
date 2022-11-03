package kitchenpos.order.application.dto.response;

import java.math.BigDecimal;

public class OrderLineItemResponse {

    private Long seq;
    private Long orderId;
    private String name;
    private BigDecimal price;
    private long quantity;

    public OrderLineItemResponse() {
    }

    public OrderLineItemResponse(Long seq, Long orderId, String name, BigDecimal price, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
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
