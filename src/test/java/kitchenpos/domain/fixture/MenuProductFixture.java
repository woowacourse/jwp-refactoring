package kitchenpos.domain.fixture;

import java.math.BigDecimal;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;

@SuppressWarnings("NonAsciiCharacters")
public class MenuProductFixture {

    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;
    private Price price;

    private MenuProductFixture() {
    }

    public static MenuProduct 상품_하나(final Long productId) {
        return 메뉴_그룹()
            .상품_아이디(productId)
            .수량(1)
            .build();
    }

    public static MenuProduct 가격_정보가_있는_상품_하나(final Long productId, final BigDecimal price) {
        return 메뉴_그룹()
            .상품_아이디(productId)
            .가격(price)
            .수량(1)
            .build();
    }

    private static MenuProductFixture 메뉴_그룹() {
        return new MenuProductFixture();
    }

    private MenuProductFixture 상품_아이디(final Long productId) {
        this.productId = productId;
        return this;
    }

    private MenuProductFixture 가격(final BigDecimal price) {
        this.price = new Price(price);
        return this;
    }

    private MenuProductFixture 수량(final int quantity) {
        this.quantity = quantity;
        return this;
    }

    private MenuProduct build() {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        menuProduct.setPrice(price);
        return menuProduct;
    }
}
