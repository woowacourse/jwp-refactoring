package kitchenpos.domain.menu;

import kitchenpos.exception.KitchenposException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

import static kitchenpos.exception.ExceptionInformation.*;

@Embeddable
public class Price {
    public static final BigDecimal MIN_PRICE = new BigDecimal(0);
    public static final BigDecimal MAX_PRICE = new BigDecimal(Integer.MAX_VALUE);
    public static final int MAX_SCALE = 2;

    @Column(nullable = false, columnDefinition = "decimal")
    private BigDecimal price;

    protected Price(){
    }

    public Price(final BigDecimal price) {
        this.price = price;
    }

    public static Price from(final BigDecimal price) {
        validateNotNull(price);
        validateBound(price);
        validateScale(price);
        return new Price(price);
    }

    private static void validateNotNull(final BigDecimal price) {
        if(Objects.isNull(price)){
            throw new KitchenposException(MENU_PRICE_IS_NULL);
        }
    }

    private static void validateBound(final BigDecimal price) {
        if (price.compareTo(MIN_PRICE) < 0 || price.compareTo(MAX_PRICE) > 0) {
            throw new KitchenposException(MENU_PRICE_LENGTH_OUT_OF_BOUNCE);
        }
    }

    private static void validateScale(final BigDecimal price) {
        if (price.scale() > MAX_SCALE) {
            throw new KitchenposException(MENU_PRICE_LENGTH_OUT_OF_BOUNCE);
        }
    }

    public BigDecimal multiply(BigDecimal other) {
        return this.price.multiply(other);
    }

    public void setPrice(final BigDecimal value) {
        this.price = value;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Price otherPrice = (Price) o;
        return Objects.equals(this.price, otherPrice.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}
