package kitchenpos.menu.supports;

import kitchenpos.menu.domain.model.MenuProduct;

public class MenuProductFixture {

    private Long seq = null;
    private Long productId = 1L;
    private long quantity = 3L;

    private MenuProductFixture() {
    }

    public static MenuProductFixture fixture() {
        return new MenuProductFixture();
    }

    public MenuProductFixture seq(Long seq) {
        this.seq = seq;
        return this;
    }

    public MenuProductFixture productId(Long productId) {
        this.productId = productId;
        return this;
    }

    public MenuProductFixture quantity(long quantity) {
        this.quantity = quantity;
        return this;
    }

    public MenuProduct build() {
        return new MenuProduct(seq, productId, quantity);
    }
}
