package kitchenpos.domain.menu;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu")
    List<MenuProduct> menuProducts;

    protected MenuProducts() {
    }

    public MenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public static MenuProducts create(final List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    public BigDecimal getPrice() {
        return menuProducts.stream()
                .map(MenuProduct::getPrice)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void updateMenu(final Menu menu) {
        menuProducts.forEach(menuProduct -> menuProduct.updateMenu(menu));
    }
}
