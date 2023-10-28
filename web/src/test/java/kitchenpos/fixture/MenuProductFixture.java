package kitchenpos.fixture;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

@SuppressWarnings("NonAsciiCharacters")
public class MenuProductFixture {

    public static MenuProduct createMenuProduct(Long seq, long quantity) {
        return MenuProduct.builder()
                .productId(Product.builder().build().getId())
                .quantity(quantity)
                .build();
    }
}
