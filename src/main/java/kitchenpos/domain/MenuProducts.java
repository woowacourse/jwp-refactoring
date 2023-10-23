package kitchenpos.domain;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.List;

public class MenuProducts {

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "menu_id")
    private List<MenuProduct> menuProducts;

    protected MenuProducts() {
    }

    private MenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public static MenuProducts create(final List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    public boolean isInvalidPrice(final BigDecimal price) {
        final double sum = menuProducts.stream()
                .mapToDouble(MenuProduct::calculateMenuProductPrice)
                .sum();
        return sum < price.doubleValue();
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
