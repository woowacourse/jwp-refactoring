package kitchenpos.dto;

import kitchenpos.domain.order.OrderLineItem;

import java.math.BigDecimal;

public class OrderLineItemResponse {

    private Long id;
    private Long menuId;
    private Long quantity;
    private String menuName;
    private BigDecimal menuPrice;

    public OrderLineItemResponse(Long id, Long menuId, Long quantity, String menuName, BigDecimal menuPrice) {
        this.id = id;
        this.menuId = menuId;
        this.quantity = quantity;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }

    public static OrderLineItemResponse toResponse(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getId(),
                orderLineItem.getMenuId(),
                orderLineItem.getQuantity(),
                orderLineItem.getMenuName(),
                orderLineItem.getMenuPrice());
    }

    public Long getId() {
        return id;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }
}
