package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuProductFixtures {

    public static MenuProduct 메뉴상품_로제떡볶이() {
        Product 로제떡볶이 = ProductFixtures.로제떡볶이();
        Menu 떡볶이메뉴 = MenuFixtures.떡볶이메뉴();
        return new MenuProduct(떡볶이메뉴, 로제떡볶이, 1L);
    }

    public static MenuProduct 메뉴상품_짜장떡볶이() {
        Product 짜장떡볶이 = ProductFixtures.짜장떡볶이();
        Menu 떡볶이메뉴 = MenuFixtures.떡볶이메뉴();
        return new MenuProduct(떡볶이메뉴, 짜장떡볶이, 1L);
    }

    public static MenuProduct 메뉴상품_마라떡볶이() {
        Product 마라떡볶이 = ProductFixtures.마라떡볶이();
        Menu 떡볶이메뉴 = MenuFixtures.떡볶이메뉴();
        return new MenuProduct(떡볶이메뉴, 마라떡볶이, 1L);
    }
}
