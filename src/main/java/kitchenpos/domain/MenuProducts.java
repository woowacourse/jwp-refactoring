package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "menu_id", nullable = false)
    private List<MenuProduct> menuProducts;

    private MenuProducts() {
    }

    public MenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public BigDecimal calculateSumOfPrice() {
        BigDecimal sumOfPrice = new BigDecimal(0);

        for (MenuProduct menuProduct : menuProducts) {
            BigDecimal price = menuProduct.calculateSumOfPrice();
            sumOfPrice = sumOfPrice.add(price);
        }
        return sumOfPrice;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
