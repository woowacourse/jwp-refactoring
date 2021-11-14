package kitchenpos.domain;

import static org.junit.jupiter.api.Assertions.assertThrows;

import kitchenpos.Fixtures;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @DisplayName("수량은 항상 0 이상어이야 한다")
    @Test
    void checkQuantity() {
        Long seq = 1L;
        Menu menu = Fixtures.makeMenu();
        Product product = Fixtures.makeProduct();
        long quantity = -1;
        assertThrows(IllegalArgumentException.class,
            () -> new MenuProduct(seq, menu, product.getId(), quantity));
    }

}
