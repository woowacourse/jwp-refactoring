package kitchenpos.common.vo;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {

    private static final int LESS_THAN_PARAMETER = -1;
    @Column(nullable = false)
    private Long quantity;

    protected Quantity() {
    }

    private Quantity(Long quantity) {
        this.quantity = quantity;
    }

    public static Quantity from(Long quantity) {
        validateNullOrNegative(quantity);

        return new Quantity(quantity);
    }

    private static void validateNullOrNegative(Long quantity) {
        if (Objects.isNull(quantity) || quantity.compareTo(BigDecimal.ZERO.longValue()) <= LESS_THAN_PARAMETER) {
            throw new IllegalArgumentException();
        }
    }

    public Long getValue() {
        return quantity;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        return Objects.equals(quantity, ((Quantity) other).quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }

}
