package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany
    @JoinColumn(name = "menu_id")
    private List<MenuProduct> menuProducts;

    protected MenuProducts() {
    }

    public MenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public MenuPrice calculatePrice() {
        final BigDecimal result = menuProducts.stream()
            .map(MenuProduct::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new MenuPrice(result);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
