package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.common.exception.CustomErrorCode;
import kitchenpos.common.exception.DomainLogicException;

@Embeddable
public class Quantity {

    @Column(name = "quantity", nullable = false)
    private long value;

    protected Quantity() {
    }

    public Quantity(final long value) {
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
