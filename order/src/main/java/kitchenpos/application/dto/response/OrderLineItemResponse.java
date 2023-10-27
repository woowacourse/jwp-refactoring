package kitchenpos.application.dto.response;

import kitchenpos.domain.OrderLineItem;

import java.math.BigDecimal;

public class OrderLineItemResponse {

    private Long seq;
    private String menuName;
    private BigDecimal price;
    private long quantity;

    public OrderLineItemResponse(Long seq, String menuName, BigDecimal price, long quantity) {
        this.seq = seq;
        this.menuName = menuName;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
                orderLineItem.getSeq(),
                orderLineItem.getMenuName(),
                orderLineItem.getPrice(),
                orderLineItem.getQuantity()
        );
    }

    public Long getSeq() {
        return seq;
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }
}
