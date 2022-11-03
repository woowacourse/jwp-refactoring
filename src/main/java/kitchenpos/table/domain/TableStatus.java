package kitchenpos.table.domain;

import kitchenpos.order.domain.OrderStatus;

public enum TableStatus {

    EAT_IN,
    EMPTY;

    public static TableStatus from(final OrderStatus orderStatus) {
        if (orderStatus.isCompleted()) {
            return EMPTY;
        }
        return EAT_IN;
    }

    public boolean isEatIn() {
        return this.equals(EAT_IN);
    }
}
