package kitchenpos.domain.menu;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.domain.common.Price;
import kitchenpos.domain.exception.InvalidMenuPriceException;
import kitchenpos.domain.exception.InvalidProductException;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<MenuProduct> values = new ArrayList<>();

    protected MenuProducts() {
    }

    private MenuProducts(final List<MenuProduct> values) {
        this.values = values;
    }

    public static MenuProducts of(
            final Price totalMenuProductPrice,
            final Price menuPrice,
            final List<MenuProduct> menuProducts
    ) {
        validateMenuProduct(menuProducts);
        validateMenuProductPrice(totalMenuProductPrice, menuPrice);

        return new MenuProducts(menuProducts);
    }

    private static void validateMenuProduct(final List<MenuProduct> menuProducts) {
        if (menuProducts == null || menuProducts.isEmpty()) {
            throw new InvalidProductException();
        }
    }

    private static void validateMenuProductPrice(final Price totalMenuProductPrice, final Price menuPrice) {
        if (menuPrice.isGreaterThan(totalMenuProductPrice)) {
            throw new InvalidMenuPriceException();
        }
    }

    public void initMenu(final Menu menu) {
        for (final MenuProduct value : values) {
            value.initMenu(menu);
        }
    }

    public List<MenuProduct> getValues() {
        return values;
    }
}
