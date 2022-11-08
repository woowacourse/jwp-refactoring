package kitchenpos.application.dto;

import java.math.BigDecimal;

public class OrderLineItemRequest {

    private Long orderId;
    private Long menuId;
    private String name;
    private BigDecimal price;
    private long quantity;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long orderId, Long menuId, String name, BigDecimal price, long quantity) {
        this.orderId = orderId;
        this.menuId = menuId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
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
