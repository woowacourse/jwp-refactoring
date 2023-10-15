package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@SuppressWarnings("NonAsciiCharacters")
public class MenuProductFixture {

    public static MenuProduct 메뉴_상품_엔티티_A = createMenuProduct(1L, 10);

    private static MenuProduct createMenuProduct(Long seq, long quantity) {
        Product 상품_엔티티_A = ProductFixture.상품_엔티티_A;

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(상품_엔티티_A.getId());
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
