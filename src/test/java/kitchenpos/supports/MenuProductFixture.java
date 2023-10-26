package kitchenpos.supports;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuProductFixture {

    private Long seq = null;
    private Menu menu = MenuFixture.fixture().build();
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

    public MenuProductFixture menu(Menu menu) {
        this.menu = menu;
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
        return new MenuProduct(seq, menu, product, quantity);
    }
}
