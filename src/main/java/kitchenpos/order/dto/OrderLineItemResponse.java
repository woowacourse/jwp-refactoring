package kitchenpos.order.dto;

import java.math.BigDecimal;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {

    private String menuName;
    private BigDecimal menuPrice;
    private Long quantity;

    private OrderLineItemResponse() {
    }

    private OrderLineItemResponse(String menuName, BigDecimal menuPrice, Long quantity) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
                orderLineItem.getMenuName(),
                orderLineItem.getMenuPrice().getValue(),
                orderLineItem.getQuantity()
        );
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }

    public Long getQuantity() {
        return quantity;
    }
}
