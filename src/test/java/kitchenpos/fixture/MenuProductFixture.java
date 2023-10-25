package kitchenpos.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

@SuppressWarnings("NonAsciiCharacters")
public class MenuProductFixture {

    public static MenuProduct createMenuProduct(Long seq, long quantity) {
        return MenuProduct.builder()
                .menu(Menu.builder().build())
                .product(Product.builder().build())
                .quantity(quantity)
                .build();
    }
}
