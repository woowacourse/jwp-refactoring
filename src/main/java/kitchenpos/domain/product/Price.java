package kitchenpos.domain.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import org.springframework.data.relational.core.mapping.Column;

public class Price implements Serializable {

    public static final int MAX_PRICE_SCALE = 2;
    public static final int MAX_PRICE_PRECISION = 19;

    @JsonProperty("price")
    @Column("PRICE")
    private final BigDecimal value;

    public Price(final BigDecimal value) {
        if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        if (value.scale() > MAX_PRICE_SCALE || value.precision() > MAX_PRICE_PRECISION) {
            throw new IllegalArgumentException();
        }

        this.value = value;
    }

    public Price multiply(final Price price) {
        return new Price(this.value.multiply(price.value));
    }

    public Price add(final Price price) {
        return new Price(this.value.add(price.value));
    }

    public boolean isBiggerThan(final Price price) {
        return this.value.compareTo(price.value) > 0;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Price price = (Price) o;
        return Objects.equals(value, price.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
