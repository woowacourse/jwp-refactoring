package kitchenpos.order.dto.response;

import java.math.BigDecimal;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {

    private Long seq;
    private String menuName;
    private BigDecimal menuPrice;
    private long quantity;

    public OrderLineItemResponse(OrderLineItem orderLineItem) {
        seq = orderLineItem.getSeq();
        menuName = orderLineItem.getMenuName();
        menuPrice = orderLineItem.getMenuPrice();
        quantity = orderLineItem.getQuantity();
    }

    public Long getSeq() {
        return seq;
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
