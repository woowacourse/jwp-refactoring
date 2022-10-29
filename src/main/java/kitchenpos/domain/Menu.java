package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.product.Price;

public class Menu {

    private final Long id;
    private final String name;
    private final Price price;
    private final Long menuGroupId;
    private List<MenuProduct> menuProducts;

    public Menu(final Long id,
                final String name,
                final BigDecimal price,
                final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        validatePrice(price, menuProducts);
        this.id = id;
        this.name = name;
        this.price = new Price(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu(final String name,
                final BigDecimal price,
                final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    public Menu(final Long id, final String name, final Price price, final Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    private void validatePrice(final BigDecimal price, final List<MenuProduct> menuProducts) {
        final BigDecimal totalAmount = calculateAmount(menuProducts);
        if (price.compareTo(totalAmount) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private BigDecimal calculateAmount(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProduct::calculateAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
