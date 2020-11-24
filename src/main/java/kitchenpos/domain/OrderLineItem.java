package kitchenpos.domain;

import java.util.Objects;

public class OrderLineItem {

    private Long seq;
    private Long orderId;
    private Long menuId;
    private Long quantity;

    private OrderLineItem() {
    }

    public OrderLineItem(Long orderId, Long menuId, Long quantity) {
        this(null, orderId, menuId, quantity);
    }

    public OrderLineItem(Long seq, Long orderId, Long menuId, Long quantity) {
        validate(orderId, menuId, quantity);
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    private void validate(Long orderId, Long menuId, Long quantity) {
        if (Objects.isNull(orderId)) {
            throw new IllegalArgumentException("order id of OrderLineItem cannot be null.");
        }
        if (Objects.isNull(menuId)) {
            throw new IllegalArgumentException("order id of OrderLineItem cannot be null.");
        }
        validateQuantity(quantity);
    }

    private void validateQuantity(Long quantity) {
        if (Objects.isNull(quantity)) {
            throw new IllegalArgumentException("order id of OrderLineItem cannot be null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity of menu must be larger than 0.");
        }
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
