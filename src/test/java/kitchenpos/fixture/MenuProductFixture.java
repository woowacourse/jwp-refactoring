package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.ArrayList;
import kitchenpos.common.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public final class MenuProductFixture {

    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    private MenuProductFixture() {
    }

    public static MenuProductFixture builder() {
        return new MenuProductFixture();
    }

    public MenuProductFixture withSeq(Long seq) {
        this.seq = seq;
        return this;
    }

    public MenuProductFixture withMenuId(Long menuId) {
        this.menuId = menuId;
        return this;
    }

    public MenuProductFixture withProductId(Long productId) {
        this.productId = productId;
        return this;
    }

    public MenuProductFixture withQuantity(long quantity) {
        this.quantity = quantity;
        return this;
    }

    public MenuProduct build() {
        return new MenuProduct(seq,
            new Product(productId, "productName", new Price(BigDecimal.valueOf(1000))),
            new Menu(menuId, "menuName", new Price(BigDecimal.valueOf(0)), null, new ArrayList<>()),
            quantity);
    }
}
