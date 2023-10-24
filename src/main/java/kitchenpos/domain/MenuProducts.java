package kitchenpos.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "menu")
    private List<MenuProduct> menuProducts;

    public MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public void validateMenuProductsPrice(BigDecimal price, Menu menu) {
        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProduct menuProduct : this.menuProducts) {
            BigDecimal productPrice = menuProduct.getProduct().getPrice();
            sum = sum.add(productPrice.multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
            menuProduct.changeMenu(menu);
        }
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

}
