package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

import java.math.BigDecimal;

import static kitchenpos.fixture.ProductFixture.createProductWithPrice;

public class MenuProductFixture {

    private static MenuProduct createMenuProduct(Long seq, Menu menu, Product product, Long quantity) {
        return new MenuProduct(seq, menu, product, quantity);
    }

    public static MenuProduct createMenuProductWithId(Long seq) {
        return createMenuProduct(seq, MenuFixture.createMenuWithId(1L),
                createProductWithPrice(BigDecimal.ONE), 3L);
    }

    public static MenuProduct createMenuProductWithProduct(Product product) {
        return createMenuProduct(null, null, product, 1L);
    }

    public static MenuProduct createMenuProductWithProductAndQuantity(Product product, Long quantity) {
        return createMenuProduct(null, null, product, quantity);
    }

    public static MenuProduct createMenuProductWithMenu(Menu menu) {
        return createMenuProduct(null, menu, null, null);
    }

    public static MenuProduct createMenuProductWithMenuAndProduct(Menu menu, Product product) {
        return createMenuProduct(null, menu, product, 1L);
    }
}
