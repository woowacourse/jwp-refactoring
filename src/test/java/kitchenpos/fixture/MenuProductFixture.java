package kitchenpos.fixture;

import kitchenpos.menu.domain.MenuProduct;

@SuppressWarnings("NonAsciiCharacters")
public class MenuProductFixture {

    public static MenuProduct 메뉴_상품(Long savedProductId, long quantity) {
        return MenuProduct.of(
                savedProductId,
                quantity
        );
    }

    public static MenuProduct 존재하지_않는_상품을_가진_메뉴_상품() {
        return MenuProduct.of(
                ProductFixture.후추_치킨_10000원().getId(),
                0
        );
    }

}
