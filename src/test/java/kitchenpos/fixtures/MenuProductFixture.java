package kitchenpos.fixtures;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuProductFixture {

    public static MenuProduct 치즈폭탄의치즈볼(Menu menu, Product product) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(1L);
        menuProduct.setMenuId(menu.getId());
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(10);
        return menuProduct;
    }

    public static MenuProduct 치즈폭탄의아메리카노(Menu menu, Product product) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(2L);
        menuProduct.setMenuId(menu.getId());
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(1);
        return menuProduct;
    }

    public static MenuProduct 무많이뿌링클의뿌링클(Menu menu, Product product) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(1L);
        menuProduct.setMenuId(menu.getId());
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(1);
        return menuProduct;
    }

    public static MenuProduct 무많이뿌링클의콜라(Menu menu, Product product) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(2L);
        menuProduct.setMenuId(menu.getId());
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(1);
        return menuProduct;
    }

    public static MenuProduct 무많이뿌링클의치킨무(Menu menu, Product product) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(3L);
        menuProduct.setMenuId(menu.getId());
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(10);
        return menuProduct;
    }

    public static MenuProduct 둘이서알리오갈리오의알리오갈리오(Menu menu, Product product) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(1L);
        menuProduct.setMenuId(menu.getId());
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(2);
        return menuProduct;
    }

    public static MenuProduct 둘이서알리오갈리오의콜라(Menu menu, Product product) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(2L);
        menuProduct.setMenuId(menu.getId());
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(2);
        return menuProduct;
    }

    public static MenuProduct 아메리카노한잔의아메리카노(Menu menu, Product product) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(1L);
        menuProduct.setMenuId(menu.getId());
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(2);
        return menuProduct;
    }
}
