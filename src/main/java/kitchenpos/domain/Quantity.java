package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.EmptyDataException;
import kitchenpos.exception.InvalidQuantityException;

@Embeddable
public class Quantity {

    @Column(name = "quantity")
    private long value;

    protected Quantity() {
    }

    public Quantity(Long value) {
        validate(value);
        this.value = value;
    }

    private void validate(Long value) {
        if (Objects.isNull(value)) {
            throw new EmptyDataException(Quantity.class.getSimpleName());
        }
        if (value < 0) {
            throw new InvalidQuantityException();
        }
    }

    public long getValue() {
        return value;
    }
}
