package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @DisplayName("금액 계산")
    @Test
    void amount() {
        MenuGroup menuGroup = new MenuGroup("추천메뉴");
        Menu halfHalf = new Menu("후라이드+후라이드", BigDecimal.valueOf(19000), menuGroup);
        Product chicken = new Product("강정치킨", BigDecimal.valueOf(17000));
        MenuProduct menuProduct = new MenuProduct(halfHalf, chicken, 2);

        assertThat(menuProduct.amount()).isEqualTo(BigDecimal.valueOf(34000));
    }
}
