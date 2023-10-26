package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.ArrayList;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public final class OrderLineItemFixture {

    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    private OrderLineItemFixture() {
    }

    public static OrderLineItemFixture builder() {
        return new OrderLineItemFixture();
    }

    public OrderLineItemFixture withSeq(Long seq) {
        this.seq = seq;
        return this;
    }

    public OrderLineItemFixture withOrderId(Long orderId) {
        this.orderId = orderId;
        return this;
    }

    public OrderLineItemFixture withMenuId(Long menuId) {
        this.menuId = menuId;
        return this;
    }

    public OrderLineItemFixture withQuantity(long quantity) {
        this.quantity = quantity;
        return this;
    }

    public OrderLineItem build() {
        return new OrderLineItem(
            seq,
            orderId,
            new Order(orderId, null, OrderStatus.COOKING.name(), LocalDateTime.now(), new ArrayList<>()),
            quantity
        );
    }
}
