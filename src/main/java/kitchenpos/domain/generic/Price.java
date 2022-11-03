package kitchenpos.domain.generic;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    @Column(name = "price", precision = 19, scale = 2, nullable = false)
    private BigDecimal value;

    protected Price() {
    }

    public Price(BigDecimal value) {
        validatePrice(value);
        this.value = value;
    }

    public static <T> Price sum(Collection<T> bags, Function<T, Price> monetary) {
        return bags.stream()
                .map(monetary)
                .reduce(new Price(BigDecimal.ZERO), Price::plus);
    }

    private void validatePrice(BigDecimal value) {
        if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격이 유효하지 않습니다. price = " + value);
        }
    }

    public Price multiply(long times) {
        return new Price(value.multiply(BigDecimal.valueOf(times)));
    }

    public Price plus(Price price) {
        return new Price(this.value.add(price.value));
    }

    public boolean isGreaterThan(Price price) {
        return this.value.compareTo(price.value) > 0;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Price{" +
                "value=" + value +
                '}';
    }
}
