package kitchenpos.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.domain.support.PriceDeserializer;
import kitchenpos.domain.support.PriceSerializer;

@JsonSerialize(using = PriceSerializer.class)
@JsonDeserialize(using = PriceDeserializer.class)
@Embeddable
public class Price {

    public static Price ZERO = new Price(BigDecimal.ZERO);

    @Column
    private BigDecimal price;

    public Price(BigDecimal price) {
        validateValue(price);
        this.price = price;
    }

    public Price() {
    }

    private void validateValue(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격 값이 유효하지 않습니다.");
        }
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Price sum(Price second) {
        return new Price(this.price.add(second.price));
    }

    public boolean isGreater(Price second) {
        return this.price.compareTo(second.price) > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Price price1 = (Price) o;
        return price.compareTo(price1.price) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}
