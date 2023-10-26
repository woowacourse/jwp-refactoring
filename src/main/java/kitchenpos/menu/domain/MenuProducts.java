package kitchenpos.menu.domain;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.EAGER;

import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.menu.vo.Price;

@Embeddable
public class MenuProducts {

    @OneToMany(cascade = PERSIST, fetch = EAGER)
    @JoinColumn(name = "menu_id", updatable = false, nullable = false)
    private List<MenuProduct> menuProducts;

    MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    protected MenuProducts() {
    }

    public static MenuProducts from(List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    public Price calculateMenuProductsTotalPrice() {
        return menuProducts.stream()
                .map(MenuProduct::calculateTotalPrice)
                .reduce(Price.ZERO, Price::add);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
