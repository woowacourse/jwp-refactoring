package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import kitchenpos.util.BigDecimalUtil;

public class Menu {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProduct> menuProducts;

    private Menu(final Long id,
                 final String name,
                 final BigDecimal price,
                 final Long menuGroupId,
                 final List<MenuProduct> menuProducts) {
        validatePrice(price);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = new ArrayList<>(menuProducts);
    }

    public static Builder builder(final String name, final BigDecimal price, final Long menuGroupId) {
        return new Builder(name, price, menuGroupId);
    }

    private void validatePrice(final BigDecimal bigDecimal) {
        BigDecimalUtil.valueForCompare(bigDecimal)
                .throwIfNegative(IllegalArgumentException::new);
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
        return new ArrayList<>(menuProducts);
    }

    public void addMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts.addAll(menuProducts);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Menu menu = (Menu) o;
        if (Objects.isNull(this.id) || Objects.isNull(menu.id)) {
            return false;
        }
        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static class Builder {

        private Long id;
        private final String name;
        private final BigDecimal price;
        private final Long menuGroupId;
        private List<MenuProduct> menuProducts = Collections.emptyList();

        public Builder(final String name, final BigDecimal price, final Long menuGroupId) {
            this.name = name;
            this.price = price;
            this.menuGroupId = menuGroupId;
        }

        public Builder id(final Long id) {
            this.id = id;
            return this;
        }

        public Builder menuProducts(final List<MenuProduct> menuProducts) {
            this.menuProducts = menuProducts;
            return this;
        }

        public Menu build() {
            return new Menu(id, name, price, menuGroupId, menuProducts);
        }
    }
}
