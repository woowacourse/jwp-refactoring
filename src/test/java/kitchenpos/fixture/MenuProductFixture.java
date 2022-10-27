package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;

public class MenuProductFixture {

    public static MenuProductRequest generateMemberProductRequest(final Product product, final int quantity) {
        return new MenuProductRequest(product.getId(), quantity);
    }

    public static MenuProduct generateMemberProduct(final Product product, final int quantity) {
        return new MenuProduct(product.getId(), quantity);
    }
}
