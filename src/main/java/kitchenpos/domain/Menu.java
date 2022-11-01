package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class Menu {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final MenuProducts menuProducts;

    public Menu(final Long id, final String name, final BigDecimal price, final Long menuGroupId,
                final MenuProducts menuProducts) {
        validatePrice(price);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu(final Long id, final String name, final BigDecimal price, final Long menuGroupId) {
        this(id, name, price, menuGroupId, null);
    }

    public Menu(final String name, final BigDecimal price, final Long menuGroupId,
                final MenuProducts menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    private void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
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

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getValue();
    }

    public Menu setMenuProducts(final MenuProducts menuProducts) {
        return new Menu(id, name, price, menuGroupId, menuProducts);
    }
}
