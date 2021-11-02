package kitchenpos.fixture;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {

    public MenuProduct 메뉴_상품_생성(Long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    public MenuProduct 메뉴_상품_생성(Long seq, Long menuId, Long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    public List<MenuProduct> 메뉴_상품_리스트_생성(MenuProduct... menuProducts) {
        return Arrays.asList(menuProducts);
    }
}
