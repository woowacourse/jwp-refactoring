package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.Test;

class MenuTest {
    @Test
    void 메뉴의_가격은_음수일_수_없다() {
        MenuGroup menuGroup = new MenuGroup("메뉴그룹");
        assertThatThrownBy(() -> new Menu("메뉴1", new BigDecimal(-1), menuGroup, new ArrayList<>()));
    }

    @Test
    void 메뉴의_가격이_상품의_총_가격보다_클_수_없다() {
        MenuGroup menuGroup = new MenuGroup("메뉴그룹");
        MenuProduct menuProduct1 = new MenuProduct(new Product("상품1", new BigDecimal(1000)), 1);
        MenuProduct menuProduct2 = new MenuProduct(new Product("상품1", new BigDecimal(1000)), 2);
        assertThatThrownBy(() -> new Menu("메뉴1", new BigDecimal(3001), menuGroup, List.of(menuProduct1, menuProduct2)));
    }
}
