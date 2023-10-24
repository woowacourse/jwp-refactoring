package kitchenpos.domain.menu;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.domain.common.Price;
import kitchenpos.domain.exception.InvalidMenuPriceException;
import kitchenpos.domain.exception.InvalidMenuProductException;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<MenuProduct> values = new ArrayList<>();

    protected MenuProducts() {
    }

    private MenuProducts(final List<MenuProduct> values) {
        this.values = values;
    }

    public static MenuProducts of(final Menu menu, final List<MenuProduct> menuProducts) {
        validateMenuProduct(menuProducts);
        validateMenuProductPrice(menu.price(), menuProducts);
        initMenuForMenuProduct(menu, menuProducts);

        return new MenuProducts(menuProducts);
    }

    private static void validateMenuProduct(final List<MenuProduct> menuProducts) {
        if (menuProducts == null || menuProducts.isEmpty()) {
            throw new InvalidMenuProductException();
        }
    }

    private static void validateMenuProductPrice(final Price menuPrice, final List<MenuProduct> menuProducts) {
        final Price totalMenuProductPrice = calculateTotalMenuProductPrice(menuProducts);

        if (menuPrice.compareTo(totalMenuProductPrice) > 0) {
            throw new InvalidMenuPriceException();
        }
    }

    private static Price calculateTotalMenuProductPrice(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                           .map(menuProduct -> menuProduct.productPrice().times(menuProduct.getQuantity()))
                           .reduce(Price.ZERO, Price::plus);
    }

    private static void initMenuForMenuProduct(final Menu menu, final List<MenuProduct> menuProducts) {
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.initMenu(menu);
        }
    }

    public List<MenuProduct> getValues() {
        return values;
    }
}
