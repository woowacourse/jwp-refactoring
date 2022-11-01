package kitchenpos.domain;

import java.util.Arrays;
import kitchenpos.exception.CustomError;
import kitchenpos.exception.DomainLogicException;

public enum OrderStatus {

    COOKING, MEAL, COMPLETION;

    public static OrderStatus from(final String status) {
        return Arrays.stream(OrderStatus.values())
                .filter(value -> value.name().equals(status))
                .findFirst()
                .orElseThrow();
    }

    public OrderStatus change(final OrderStatus status) {
        validateNotSame(status);
        if (status != COMPLETION) {
            validateUncompleted();
        }
        return status;
    }

    private void validateNotSame(final OrderStatus status) {
        if (this == status) {
            throw new DomainLogicException(CustomError.ORDER_STATUS_CHANGE_SAME_ERROR);
        }
    }

    private void validateUncompleted() {
        if (this == COMPLETION) {
            throw new DomainLogicException(CustomError.ORDER_STATUS_ALREADY_COMPLETED_ERROR);
        }
    }

    public boolean isSame(final OrderStatus status) {
        return this == status;
    }
}
