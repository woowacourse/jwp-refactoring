package kitchenpos.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MenuProducts {

    private final List<MenuProduct> menuProducts;

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = new ArrayList<>(menuProducts);
    }

    public Price getTotalProductsPrice() {
        Price price = new Price();
        for (MenuProduct menuProduct : menuProducts) {
            price.add(menuProduct.getProductsPrice());
        }
        return price;
    }

    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }
}
