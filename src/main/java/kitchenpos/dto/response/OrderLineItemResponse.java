package kitchenpos.dto.response;

import java.math.BigDecimal;
import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemResponse {
    private String menuName;
    private BigDecimal menuPrice;
    private long quantity;

    private OrderLineItemResponse() {
    }

    public OrderLineItemResponse(final OrderLineItem orderLineItem) {
        this(orderLineItem.getMenuName(), orderLineItem.getMenuPrice(), orderLineItem.getQuantity());
    }

    public OrderLineItemResponse(final String menuName, final BigDecimal menuPrice, final long quantity) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
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
