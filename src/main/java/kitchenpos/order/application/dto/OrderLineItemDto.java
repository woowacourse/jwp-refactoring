package kitchenpos.order.application.dto;

import java.math.BigDecimal;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemDto {

    private Long seq;
    private Long orderId;
    private String menuName;
    private BigDecimal menuPrice;
    private long quantity;

    private OrderLineItemDto() {
    }

    public OrderLineItemDto(final Long seq, final Long orderId, final String menuName,
                            final BigDecimal menuPrice, final long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
    }

    public static OrderLineItemDto of(final OrderLineItem orderLineItem) {
        return new OrderLineItemDto(
                orderLineItem.getSeq(),
                orderLineItem.getOrderId(),
                orderLineItem.getMenuName(),
                orderLineItem.getMenuPrice(),
                orderLineItem.getQuantity()
        );
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }

    public long getQuantity() {
        return quantity;
    }
}
