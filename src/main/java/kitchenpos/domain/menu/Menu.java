package kitchenpos.domain.menu;

import kitchenpos.domain.menuproduct.MenuProduct;

import java.util.List;
import java.util.Objects;

public class Menu {
    private Long id;
    private MenuName name;
    private MenuPrice price;
    private Long menuGroupId;
    private MenuProducts menuProducts;

    public Menu(final MenuName name,
                final MenuPrice price,
                final Long menuGroupId) {
        this(null, name, price, menuGroupId);
    }

    public Menu(final Long id,
                final MenuName name,
                final MenuPrice price,
                final Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public Long getId() {
        return id;
    }

    public void updateMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = new MenuProducts(menuProducts);
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
        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
