package kitchenpos.domain.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import org.springframework.data.relational.core.mapping.Column;

public class Price implements Serializable {
    @JsonProperty("price")
    @Column("PRICE")
    private final BigDecimal value;

    public Price(final BigDecimal value) {
        if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
        this.value = value;
    }

    public BigDecimal multiply(final Price price) {
        return this.value.multiply(price.value);
    }
}
