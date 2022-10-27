package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Menu {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;

    public Menu(final Long id, final String name, final BigDecimal price, final Long menuGroupId) {
        validatePrice(price);

        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public Menu(final String name, final BigDecimal price, final Long menuGroupId) {
        this(null, name, price, menuGroupId);
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

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    private void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }
}
