package kitchenpos.menu.domain;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.Price;

@Embeddable
public class MenuProducts {

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "MENU_ID", nullable = false, updatable = false)
    private List<MenuProduct> menuProducts;

    protected MenuProducts() {
    }

    public MenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public Price calculateSum() {
        return menuProducts.stream()
                .map(MenuProduct::calculatePrice)
                .reduce(Price.ZERO_PRICE, Price::add);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
