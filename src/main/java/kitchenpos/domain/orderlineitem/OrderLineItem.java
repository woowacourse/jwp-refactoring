package kitchenpos.domain.orderlineitem;

import kitchenpos.domain.menuproduct.Quantity;

import java.util.Objects;

public class OrderLineItem {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private Quantity quantity;

    public OrderLineItem(final Long seq,
                         final Long orderId,
                         final Long menuId,
                         final Quantity quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderLineItem that = (OrderLineItem) o;
        return Objects.equals(seq, that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }
}
