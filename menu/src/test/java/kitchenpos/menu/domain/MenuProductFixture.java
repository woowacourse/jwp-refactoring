package kitchenpos.menu.domain;

import kitchenpos.menu.vo.ProductSpecification;
import kitchenpos.menu.vo.Quantity;

@SuppressWarnings("NonAsciiCharacters")
public class MenuProductFixture {

    public static MenuProduct 메뉴_상품(Long seq, Long productId, ProductSpecification productSpecification) {
        return new MenuProduct(seq, productId, Quantity.valueOf(1L), productSpecification);
    }

    public static MenuProduct 메뉴_상품(Long productId, long quantity, ProductSpecification productSpecification) {
        return new MenuProduct(productId, Quantity.valueOf(quantity), productSpecification);
    }
}
