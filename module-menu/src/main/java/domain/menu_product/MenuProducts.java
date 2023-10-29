package domain.menu_product;

import exception.MenuException.NoMenuProductsException;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_id", updatable = false, nullable = false)
    private final List<MenuProduct> menuProducts;

    protected MenuProducts() {
        menuProducts = null;
    }

    public MenuProducts(final List<MenuProduct> menuProducts) {
        validateMenuProducts(menuProducts);
        this.menuProducts = List.copyOf(menuProducts);
    }

    private void validateMenuProducts(final List<MenuProduct> menuProducts) {
        if (Objects.isNull(menuProducts) || menuProducts.isEmpty()) {
            throw new NoMenuProductsException();
        }
    }

    public List<MenuProduct> menuProducts() {
        return menuProducts;
    }
}
