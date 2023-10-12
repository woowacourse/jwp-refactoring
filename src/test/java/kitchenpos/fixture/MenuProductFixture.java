package kitchenpos.fixture;

import static kitchenpos.fixture.ProductFixture.후추_치킨_10000원;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {

    public static MenuProduct 후추_치킨_2개() {
        return new MenuProduct(후추_치킨_10000원().getId(), 2);
    }

}
