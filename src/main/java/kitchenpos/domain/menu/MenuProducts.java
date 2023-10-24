package kitchenpos.domain.menu;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.EAGER;

import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.vo.Money;

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

    public Money calculateMenuProductsTotalPrice() {
        return menuProducts.stream()
                .map(MenuProduct::calculateTotalPrice)
                .reduce(Money.ZERO, Money::add);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
