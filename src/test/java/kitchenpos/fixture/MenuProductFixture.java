package kitchenpos.fixture;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.dto.request.MenuProductRequest;

public class MenuProductFixture {

    public static MenuProductRequest generateMemberProductRequest(final Product product, final int quantity) {
        return new MenuProductRequest(product.getId(), quantity);
    }

    public static MenuProduct generateMemberProduct(final Product product, final int quantity) {
        return new MenuProduct(product.getId(), quantity);
    }
}
