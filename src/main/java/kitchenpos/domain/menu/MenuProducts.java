package kitchenpos.domain.menu;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public void add(final MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
    }

    public Price calculateTotalPrice() {
        return menuProducts.stream()
                           .map(MenuProduct::calculateMenuProductPrice)
                           .reduce(Price.ZERO, Price::add);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
