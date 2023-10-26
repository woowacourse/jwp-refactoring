package kitchenpos.menu;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.exception.MenuProductException.MinimumPriceException;
import kitchenpos.exception.MenuProductException.MinimumQuantityException;
import kitchenpos.exception.MenuProductException.NoMenuProductNameException;

@Entity
public class MenuProduct {

    private static final int MINIMUM_PRICE = 1000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String name;

    private BigDecimal price;

    private Long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final String name, final BigDecimal price, final Long quantity) {
        validate(name, price, quantity);
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    private void validate(final String name, final BigDecimal price, final Long quantity) {
        validateName(name);
        validatePrice(price);
        validateQuantity(quantity);
    }

    private void validateName(final String name) {
        if (Objects.isNull(name) || name.isEmpty()) {
            throw new NoMenuProductNameException();
        }
    }

    private void validatePrice(final BigDecimal price) {
        if (price.compareTo(BigDecimal.valueOf(MINIMUM_PRICE)) < 0) {
            throw new MinimumPriceException();
        }
    }

    private void validateQuantity(final Long quantity) {
        if (quantity < 1) {
            throw new MinimumQuantityException();
        }
    }

    public BigDecimal calculateTotalPrice() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    public Long getSeq() {
        return seq;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }
}
