package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<MenuProduct> value = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(final List<MenuProduct> menuProducts, final Menu menu) {
        if (menuProducts == null) {
            this.value = new ArrayList<>();
            return;
        }
        this.value = menuProducts;
        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.changeMenu(menu);
        }
    }

    public BigDecimal calculateEntirePrice() {
        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProduct menuProduct : value) {
            sum = sum.add(menuProduct.calculatePrice());
        }
        return sum;
    }

    public List<MenuProduct> getValue() {
        return value;
    }
}
