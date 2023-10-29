package kitchenpos.dto.response;

import kitchenpos.domain.order.OrderLineItem;

import java.math.BigDecimal;

public class OrderLineItemResponse {
    private final Long seq;
    private final Long orderId;
    private final String name;
    private final BigDecimal price;
    private final long quantity;

    private OrderLineItemResponse(Long seq, Long orderId, String name, BigDecimal price, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse of(Long orderId, OrderLineItem orderLineItem) {
        return OrderLineItemResponse.builder()
                .orderId(orderId)
                .seq(orderLineItem.getSeq())
                .quantity(orderLineItem.getQuantity())
                .name(orderLineItem.getName())
                .price(orderLineItem.getPrice())
                .build();
    }

    public static OrderLineItemResponseBuilder builder() {
        return new OrderLineItemResponseBuilder();
    }

    public static final class OrderLineItemResponseBuilder {
        private Long seq;
        private Long orderId;
        private String name;
        private BigDecimal price;
        private long quantity;

        private OrderLineItemResponseBuilder() {
        }

        public OrderLineItemResponseBuilder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public OrderLineItemResponseBuilder orderId(Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public OrderLineItemResponseBuilder quantity(long quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderLineItemResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public OrderLineItemResponseBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public OrderLineItemResponse build() {
            return new OrderLineItemResponse(seq, orderId, name, price, quantity);
        }
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

    public String getPrice() {
        return price.toString();
    }

    public long getQuantity() {
        return quantity;
    }
}
