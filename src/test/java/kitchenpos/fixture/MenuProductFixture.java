package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

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
