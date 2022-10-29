package kitchenpos.support;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public abstract class MenuProductFixture {

    public static List<MenuProduct> menuProductOne(long productId, long quantity) {

        Product product = new Product(productId, "상품" , BigDecimal.valueOf(quantity));

        return List.of(new MenuProduct(null, null, product, quantity));
    }

    public static List<MenuProduct> menuProducts(long productId1, long quantity1, long productId2, long quantity2) {

        Product product1 = new Product(productId1, "상품" , BigDecimal.valueOf(quantity1));
        Product product2 = new Product(productId2, "상품" , BigDecimal.valueOf(quantity2));

        return List.of(new MenuProduct(product1, quantity1), new MenuProduct(product2, quantity2));
    }
}
