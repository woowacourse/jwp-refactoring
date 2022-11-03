package kitchenpos.dto.response;

import java.math.BigDecimal;
import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemResponse {

    private Long seq;
    private Long orderId;
    private String menuName;
    private BigDecimal menuPrice;
    private long quantity;

    public OrderLineItemResponse(Long seq, Long orderId, String menuName, BigDecimal menuPrice, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getOrderId(),
                orderLineItem.getMenuName(), orderLineItem.getMenuPrice(), orderLineItem.getQuantity());
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
