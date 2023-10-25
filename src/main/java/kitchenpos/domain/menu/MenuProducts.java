package kitchenpos.domain.menu;

import kitchenpos.domain.common.Price;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
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
            final Price productPrice = product.getPrice();
            sum = sum.add(productPrice.multiply(menuProduct.getQuantity()));
        }

        return sum;
    }
}
