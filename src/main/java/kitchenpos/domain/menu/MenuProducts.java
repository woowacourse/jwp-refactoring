package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_id")
    private List<MenuProduct> menuProducts;

    public MenuProducts() {
    }

    public MenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public BigDecimal sumPrice() {
        return menuProducts.stream()
                .map(MenuProduct::calculatePrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
