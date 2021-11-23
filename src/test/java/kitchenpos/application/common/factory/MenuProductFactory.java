package kitchenpos.application.common.factory;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuProductFactory {

    private MenuProductFactory() {
    }

    public static MenuProduct create(Product product, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
