package kitchenpos.menu.supports;

import kitchenpos.menu.domain.model.MenuProduct;
import kitchenpos.product.domain.model.Product;
import kitchenpos.product.supports.ProductFixture;

public class MenuProductFixture {

    private Long seq = null;
    private Product product = ProductFixture.fixture().build();
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

    public MenuProductFixture product(Product product) {
        this.product = product;
        return this;
    }

    public MenuProductFixture quantity(long quantity) {
        this.quantity = quantity;
        return this;
    }

    public MenuProduct build() {
        return new MenuProduct(seq, product, quantity);
    }
}
