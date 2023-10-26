package kitchenpos.order.application.dto;

import kitchenpos.order.domain.OrderLineItem;

import java.math.BigDecimal;

public class OrderLineItemDto {

    private Long seq;
    private Long quantity;
    private String name;
    private BigDecimal price;

    public OrderLineItemDto(Long seq, Long quantity, String name, BigDecimal price) {
        this.seq = seq;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
    }

    public static OrderLineItemDto from(OrderLineItem orderLineItem) {
        Long seq = orderLineItem.getSeq();
        Long quantity = orderLineItem.getQuantity();
        String name = orderLineItem.getName();
        BigDecimal price = orderLineItem.getPrice();
        return new OrderLineItemDto(seq, quantity, name, price);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
