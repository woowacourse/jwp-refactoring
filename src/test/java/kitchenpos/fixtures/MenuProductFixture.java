package kitchenpos.fixtures;

import kitchenpos.domain.MenuProduct;

import static kitchenpos.fixtures.MenuFixture.*;
import static kitchenpos.fixtures.ProductFixture.*;

public class MenuProductFixture {

    public static MenuProduct 치즈폭탄의치즈볼() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(1L);
        menuProduct.setProductId(쫀득쫀득치즈볼().getId());
        menuProduct.setMenuId(치즈폭탄().getId());
        menuProduct.setQuantity(10);
        return menuProduct;
    }

    public static MenuProduct 치즈폭탄의아메리카노() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(2L);
        menuProduct.setProductId(아메리카노().getId());
        menuProduct.setMenuId(치즈폭탄().getId());
        menuProduct.setQuantity(1);
        return menuProduct;
    }

    public static MenuProduct 무많이뿌링클의뿌링클() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(1L);
        menuProduct.setProductId(맛있는뿌링클().getId());
        menuProduct.setMenuId(무많이뿌링클().getId());
        menuProduct.setQuantity(1);
        return menuProduct;
    }

    public static MenuProduct 무많이뿌링클의콜라() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(2L);
        menuProduct.setProductId(시원한콜라().getId());
        menuProduct.setMenuId(무많이뿌링클().getId());
        menuProduct.setQuantity(1);
        return menuProduct;
    }

    public static MenuProduct 무많이뿌링클의치킨무() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(3L);
        menuProduct.setProductId(치킨무().getId());
        menuProduct.setMenuId(무많이뿌링클().getId());
        menuProduct.setQuantity(10);
        return menuProduct;
    }

    public static MenuProduct 둘이서알리오갈리오의알리오갈리오() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(1L);
        menuProduct.setProductId(알리오갈리오().getId());
        menuProduct.setMenuId(둘이서알리오갈리오().getId());
        menuProduct.setQuantity(2);
        return menuProduct;
    }

    public static MenuProduct 둘이서알리오갈리오의콜라() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(2L);
        menuProduct.setProductId(시원한콜라().getId());
        menuProduct.setMenuId(둘이서알리오갈리오().getId());
        menuProduct.setQuantity(2);
        return menuProduct;
    }

    public static MenuProduct 아메리카노한잔의아메리카노() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(1L);
        menuProduct.setProductId(아메리카노().getId());
        menuProduct.setMenuId(아메리카노한잔().getId());
        menuProduct.setQuantity(2);
        return menuProduct;
    }
}
