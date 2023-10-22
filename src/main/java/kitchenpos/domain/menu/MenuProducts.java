package kitchenpos.domain.menu;

import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.domain.vo.Price;
import kitchenpos.exception.MenuException.NoMenuNameException;

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
            throw new NoMenuNameException();
        }
    }

    public Price getTotalPrice() {
        return menuProducts
                .stream()
                .map(MenuProduct::getPrice)
                .reduce(Price.ZERO, Price::add);
    }

    public List<MenuProduct> menuProducts() {
        return menuProducts;
    }
}
