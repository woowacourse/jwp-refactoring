package kitchenpos.domain;

import java.util.Objects;

import kitchenpos.exception.OrderNotExistException;

public class OrderMenu {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public OrderMenu() {
    }

    public OrderMenu(Long orderId, Long menuId, long quantity) {
        this(null, orderId, menuId, quantity);
    }

    public OrderMenu(Long seq, Long orderId, Long menuId, long quantity) {
        if (Objects.isNull(orderId)) {
            throw new OrderNotExistException();
        }

        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
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
