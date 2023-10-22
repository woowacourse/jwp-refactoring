package kitchenpos.fixture;

import kitchenpos.application.dto.request.MenuProductRequest;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@SuppressWarnings("NonAsciiCharacters")
public class MenuProductFixture {

    public static MenuProduct 메뉴상품_망고_2개() {
        return new MenuProduct(ProductFixture.상품_망고_1000원(), 2L);
    }

    public static MenuProduct 메뉴상품_치킨_1개() {
        return new MenuProduct(ProductFixture.상품_치킨_15000원(), 1L);
    }

    public static MenuProduct 메뉴상품_생성(final Product product, final Long quantity) {
        return new MenuProduct(product, quantity);
    }

    public static MenuProductRequest 메뉴상품요청_생성(final MenuProduct menuProduct) {
        return new MenuProductRequest(menuProduct.getProduct().getId(), menuProduct.getQuantity());
    }
}
