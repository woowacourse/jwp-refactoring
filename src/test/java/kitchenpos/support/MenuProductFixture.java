package kitchenpos.support;

import java.util.List;
import kitchenpos.domain.MenuProduct;

public abstract class MenuProductFixture {

    public static List<MenuProduct> menuProductOne(final long productId, final long quantity) {
        return List.of(new MenuProduct(productId, quantity));
    }

    public static List<MenuProduct> menuProducts(final long productId1, final long quantity1, final long productId2, final long quantity2) {
        return List.of(new MenuProduct(productId1, quantity1), new MenuProduct(productId2, quantity2));
    }
}
