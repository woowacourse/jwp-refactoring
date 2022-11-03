package kitchenpos.domain.table;

import kitchenpos.domain.order.OrderStatus;

public enum TableStatus {
    READY,
    USING
    ;

    public static TableStatus valueOf(final OrderStatus orderStatus) {
        if (orderStatus.equals(OrderStatus.COMPLETION)) {
            return READY;
        }
        return USING;
    }
}
