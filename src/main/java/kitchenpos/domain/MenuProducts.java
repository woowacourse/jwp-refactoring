package kitchenpos.domain;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = ALL, fetch = EAGER)
    private List<MenuProduct> items;

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> items) {
        this.items = items;
    }

    public void setMenuToAll(Menu menu) {
        items.forEach(it -> it.setMenu(menu));
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
