package kitchenpos.menu.domain;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(cascade = ALL, fetch = EAGER)
    @JoinColumn(name = "menu_id", nullable = false, updatable = false)
    private List<MenuProduct> items;

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> items) {
        this.items = items;
    }

    public Price calculateTotalPrice() {
        Price sum = new Price(BigDecimal.ZERO);
        for (MenuProduct menuProduct : items) {
            Price productPrice = menuProduct.product().price();
            sum = sum.add(productPrice.multiply(menuProduct.quantity()));
        }
        return sum;
    }

    public List<MenuProduct> items() {
        return items;
    }
}
