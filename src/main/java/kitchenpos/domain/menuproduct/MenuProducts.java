package kitchenpos.domain.menuproduct;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST)
    private List<MenuProduct> menuProducts;

    public MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public static MenuProducts of(BigDecimal price, List<MenuProduct> menuProducts) {
        check(price, menuProducts);
        return new MenuProducts(menuProducts);
    }

    public static void check(BigDecimal price, List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final BigDecimal amount = menuProduct.getAmount();
            sum = sum.add(amount);
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
