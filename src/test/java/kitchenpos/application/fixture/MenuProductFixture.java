package kitchenpos.application.fixture;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuProductFixture {

    public static MenuProduct createNotExistsProduct() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(10000L);

        return menuProduct;
    }

    public static List<MenuProduct> create(List<Product> price) {
        return price.stream()
            .map(pro -> {
                MenuProduct menuProduct = new MenuProduct();
                menuProduct.setProductId(pro.getId());
                menuProduct.setQuantity(1);
                return menuProduct;
            })
            .collect(Collectors.toList());
    }
}
