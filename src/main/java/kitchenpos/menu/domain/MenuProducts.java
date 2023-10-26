package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<MenuProduct> products;

    protected MenuProducts() {
    }

    public MenuProducts(final List<MenuProduct> products) {
        this.products = products;
    }

    public BigDecimal getTotalPrice() {
        return products.stream()
            .map(MenuProduct::getTotalPrice)
            .reduce(BigDecimal::add)
            .get();
    }

    public MenuProducts join(final Menu menu) {
        products.forEach(menuProduct -> menuProduct.join(menu));
        return new MenuProducts(products);
    }

    public List<MenuProduct> getProducts() {
        return products;
    }
}
