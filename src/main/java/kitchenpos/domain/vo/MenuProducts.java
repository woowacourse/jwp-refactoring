package kitchenpos.domain.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.domain.MenuProduct;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public boolean isPriceLessThan(BigDecimal price) {
        BigDecimal sum = menuProducts.stream()
                .map(MenuProducts::calculatePrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.compareTo(price) < 0;
    }

    private static BigDecimal calculatePrice(MenuProduct menuProduct) {
        return menuProduct.getProduct()
                .getPrice()
                .multiply(BigDecimal.valueOf(menuProduct.getQuantity()));
    }
}
