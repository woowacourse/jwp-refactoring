package kitchenpos.newdomain.vo;

import kitchenpos.exception.CustomErrorCode;
import kitchenpos.exception.DomainLogicException;

public class Quantity {

    private long value;

    public Quantity(long value) {
        validatePositive(value);
        this.value = value;
    }

    private void validatePositive(final long value) {
        if (value < 0) {
            throw new DomainLogicException(CustomErrorCode.QUANTITY_NEGATIVE_ERROR);
        }
    }

    public long getValue() {
        return value;
    }
}
