package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menuId")
    private final List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts.addAll(menuProducts);
    }

    public void setMenuId(final Long menuId) {
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenuId(menuId);
        }
    }

    public List<MenuProduct> getValues() {
        return List.copyOf(menuProducts);
    }
}
