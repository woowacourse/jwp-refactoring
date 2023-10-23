package kitchenpos.domain;

import kitchenpos.exception.menuProductException.InvalidMenuProductsPriceException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_id", nullable = false, updatable = false)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(final List<MenuProduct> menuProducts, final BigDecimal price) {
        validateMenuProducts(menuProducts, price);
        this.menuProducts = menuProducts;
    }

    private void validateMenuProducts(final List<MenuProduct> menuProducts, final BigDecimal price) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            sum = sum.add(menuProduct.getProduct().getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new InvalidMenuProductsPriceException();
        }
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
