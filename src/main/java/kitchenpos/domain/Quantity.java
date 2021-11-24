package kitchenpos.domain;

import kitchenpos.exception.quantity.InvalideQuantityValueException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Quantity {
    @Column(name = "quantity", nullable = false)
    private Long quantity;

    protected Quantity() {
    }

    private Quantity(Long quantity) {
        this.quantity = quantity;
    }

    public static Quantity from(Long quantity) {
        validate(quantity);
        return new Quantity(quantity);
    }

    private static void validate(Long quantity) {
        if (Objects.isNull(quantity) || quantity < 0L) {
            throw new InvalideQuantityValueException();
        }
    }

    public Long longValue() {
        return quantity;
    }
}
