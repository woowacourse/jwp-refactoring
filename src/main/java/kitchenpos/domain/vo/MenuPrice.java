package kitchenpos.domain.vo;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.badrequest.PriceInvalidException;

@Embeddable
public class MenuPrice {
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    protected MenuPrice() {
    }

    private MenuPrice(final BigDecimal price) {
        if (Objects.isNull(price) || isLessThanZero(price)) {
            throw new PriceInvalidException();
        }

        this.price = price;
    }

    public static MenuPrice from(final BigDecimal price) {
        return new MenuPrice(price);
    }

    public static MenuPrice from(final String price) {
        return MenuPrice.from(new BigDecimal(price));
    }

    public boolean isLessThanZero() {
        return this.price.compareTo(BigDecimal.ZERO) < 0;
    }

    private boolean isLessThanZero(final BigDecimal price) {
        return price.compareTo(BigDecimal.ZERO) < 0;
    }

    public BigDecimal getValue() {
        return price;
    }

    public MenuPrice multiply(final long quantity) {
        return new MenuPrice(this.price.multiply(BigDecimal.valueOf(quantity)));
    }
}
