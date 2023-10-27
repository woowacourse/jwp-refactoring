package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void add(final MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
    }

    public long calculateTotalPrice() {
        return menuProducts.stream()
                .mapToLong(MenuProduct::calculatePrice)
                .sum();
    }

    protected MenuProducts() {
    }
}
