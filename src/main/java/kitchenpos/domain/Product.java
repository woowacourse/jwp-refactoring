package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import kitchenpos.domain.exception.ProductException.NoPriceException;
import kitchenpos.domain.exception.ProductException.NoProductNameException;
import org.springframework.util.StringUtils;

public class Product {
    private static final int PRODUCT_PRICE_COMPARE_CONDITION = 0;
    public static final int MINIMUM_PRODUCT_PRICE = 0;
    public static final int MINIMUM_PRODUCT_NAME_LENGTH = 1;

    private Long id;
    private String name;
    private BigDecimal price;

    public Product(final String name, final BigDecimal price) {
        this(null, name, price);
    }

    public Product(final Long id, final String name, final BigDecimal price) {
        validate(name, price);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    private void validate(final String name, final BigDecimal price) {
        validateName(name);
        validatePrice(price);
    }

    private void validateName(final String name) {
        if (!StringUtils.hasText(name)) {
            throw new NoProductNameException();
        }
    }

    private void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price)
                || price.compareTo(BigDecimal.valueOf(MINIMUM_PRODUCT_PRICE)) < PRODUCT_PRICE_COMPARE_CONDITION) {
            throw new NoPriceException(price);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
