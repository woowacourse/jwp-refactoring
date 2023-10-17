package kitchenpos.application;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuProductFixtures {

    public static MenuProduct 메뉴상품_로제떡볶이() {
        Product 로제떡볶이 = ProductFixtures.로제떡볶이();
        return new MenuProduct(1L, 1L, 로제떡볶이.getId(), 1L);
    }

    public static MenuProduct 메뉴상품_짜장떡볶이() {
        Product 짜장떡볶이 = ProductFixtures.짜장떡볶이();
        return new MenuProduct(2L, 1L, 짜장떡볶이.getId(), 1L);
    }
}
