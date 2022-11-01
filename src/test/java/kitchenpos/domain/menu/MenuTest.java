package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.Test;

class MenuTest {
    @Test
    void 메뉴를_생성할_수_있다() {
        MenuProduct menuProduct = new MenuProduct(new Product("상품1", new BigDecimal(1000)), 1);
        assertDoesNotThrow(() -> new Menu("메뉴1", new BigDecimal(1000), 1L, List.of(menuProduct)));
    }

    @Test
    void 메뉴의_가격은_음수일_수_없다() {
        MenuGroup menuGroup = new MenuGroup("메뉴그룹");
        assertThatThrownBy(() -> new Menu("메뉴1", new BigDecimal(-1), 1L, new ArrayList<>()));
    }

    @Test
    void 메뉴의_가격이_상품의_총_가격보다_클_수_없다() {
        MenuGroup menuGroup = new MenuGroup("메뉴그룹");
        MenuProduct menuProduct1 = new MenuProduct(new Product("상품1", new BigDecimal(1000)), 1);
        MenuProduct menuProduct2 = new MenuProduct(new Product("상품1", new BigDecimal(1000)), 2);
        assertThatThrownBy(() -> new Menu("메뉴1", new BigDecimal(3001), 1L, List.of(menuProduct1, menuProduct2)));
    }
}
