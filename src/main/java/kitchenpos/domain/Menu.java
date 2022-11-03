package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.vo.Price;

public class Menu {
    private Long id;
    private String name;
    private Price price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    public Menu(final String name, final Price price, final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    public Menu(final Long id, final String name, final Price price, final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        validatePrice(price, menuProducts);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    private void validatePrice(final Price price, final List<MenuProduct> menuProducts) {
        price.validate();
        final BigDecimal sum = menuProducts.stream()
                .map(MenuProduct::calculateAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (!menuProducts.isEmpty() && price.isBiggerThan(sum)) {
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
        return price.getValue();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
