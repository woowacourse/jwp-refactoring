package kitchenpos.application.dto;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.domain.Order;

public class OrderLineItemRequest {

    private Long orderId;

    private Long menuId;

    private long quantity;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long orderId, Long menuId, long quantity) {
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
