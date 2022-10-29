package kitchenpos.support;

import java.util.List;
import kitchenpos.domain.MenuProduct;

public abstract class MenuProductFixture {

    public static List<MenuProduct> menuProductOne(long productId, long quantity) {
        return List.of(new MenuProduct(productId, quantity));
    }

    public static List<MenuProduct> menuProducts(long productId1, long quantity1, long productId2, long quantity2) {
        return List.of(new MenuProduct(productId1, quantity1), new MenuProduct(productId2, quantity2));
    }
}
