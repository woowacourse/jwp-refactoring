package kitchenpos.fixture;

import kitchenpos.domain.menu.MenuProduct;

public class MenuProductFixture {

    public static MenuProduct 메뉴_재고(Long menuId, Long productId, long quantity) {
        return new MenuProduct(menuId, productId, quantity);
    }
}
