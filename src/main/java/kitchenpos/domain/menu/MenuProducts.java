package kitchenpos.domain.menu;

import kitchenpos.domain.common.Price;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST)
    private final List<MenuProduct> menuProducts = new ArrayList<>();

    public MenuProducts() {
    }

    public void addAll(final List<MenuProduct> toAddMenuProducts) {
        this.menuProducts.addAll(toAddMenuProducts);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public Price getPriceSumOfProducts() {
        Price sum = Price.zero();

        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = menuProduct.getProduct();
            sum = sum.add(product.getPrice().getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        return sum;
    }
}
