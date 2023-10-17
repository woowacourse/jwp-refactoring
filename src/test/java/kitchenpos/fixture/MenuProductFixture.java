package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@SuppressWarnings("NonAsciiCharacters")
public class MenuProductFixture {

    public static MenuProduct 메뉴_상품_엔티티_A = createMenuProduct(1L, 10);

    private static MenuProduct createMenuProduct(Long seq, long quantity) {
        return MenuProduct.builder()
                .menu(Menu.builder().build())
                .product(Product.builder().build())
                .quantity(quantity)
                .build();
    }
}
