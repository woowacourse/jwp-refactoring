package com.kitchenpos.fixture;

import com.kitchenpos.domain.MenuProduct;

@SuppressWarnings("NonAsciiCharacters")
public class MenuProductFixture {

    public static MenuProduct 메뉴_상품_10개_생성(final Long productId) {
        return new MenuProduct(productId, 10L);
    }
}
