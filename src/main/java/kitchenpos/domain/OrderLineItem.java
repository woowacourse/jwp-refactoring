package kitchenpos.domain;

import lombok.Getter;

@Getter
public class OrderLineItem {

    private final Long seq;
    private final Long orderId;
    private final Long menuId;
    private final long quantity;

    public OrderLineItem(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(Long orderId, Long menuId, long quantity) {
        this(null, orderId, menuId, quantity);
    }
}
