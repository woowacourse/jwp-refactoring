package kitchenpos.domain;

import java.util.Arrays;
import kitchenpos.exception.CustomErrorCode;
import kitchenpos.exception.DomainLogicException;

public enum OrderStatus {

    COOKING, MEAL, COMPLETION;

    public static OrderStatus from(final String status) {
        return Arrays.stream(OrderStatus.values())
                .filter(value -> value.name().equals(status))
                .findFirst()
                .orElseThrow();
    }

    public OrderStatus meal() {
        validateUncompleted();
        validateNotSame(MEAL);
        return MEAL;
    }

    public OrderStatus complete() {
        validateUncompleted();
        return COMPLETION;
    }

    private void validateUncompleted() {
        if (this == COMPLETION) {
            throw new DomainLogicException(CustomErrorCode.ORDER_STATUS_ALREADY_COMPLETED_ERROR);
        }
    }

    private void validateNotSame(final OrderStatus status) {
        if (this == status) {
            throw new DomainLogicException(CustomErrorCode.ORDER_STATUS_CHANGE_SAME_ERROR);
        }
    }

    public boolean isSame(final OrderStatus status) {
        return this == status;
    }
}
