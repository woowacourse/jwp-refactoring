package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.EmptyDataException;
import kitchenpos.exception.InvalidPriceException;

@Embeddable
public class Price {

    private static final double PRICE_MIN_VALUE = 0.0;

    @Column(name = "price")
    private double value;

    private Price(double value) {
        this.value = value;
    }

    protected Price() {
    }

    public static Price from(Double value) {
        validate(value);
        return new Price(value);
    }

    private static void validate(Double value) {
        if (Objects.isNull(value)) {
            throw new EmptyDataException(Price.class.getSimpleName());
        }
        if (value < PRICE_MIN_VALUE) {
            throw new InvalidPriceException();
        }
    }

    public double getValue() {
        return value;
    }
}
