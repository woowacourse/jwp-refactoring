package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @Test
    void 생성자를_통해_객체를_생성하면_참조하는_Menu_객체에_자동으로_add한다() {
        // given
        MenuGroup menuGroup = new MenuGroup(1L, "치킨");
        Menu menu = new Menu(1L, "후라이드 치킨", BigDecimal.valueOf(1000), menuGroup);

        Product product = new Product(1L, "후라이드 치킨", BigDecimal.valueOf(1000));

        // when
        MenuProduct menuProduct = new MenuProduct(menu, product, 1);

        // then
        assertThat(menu.menuProducts().get(0)).isEqualTo(menuProduct);
    }

    @Test
    void 총합을_구한다() {
        // given
        MenuGroup menuGroup = new MenuGroup(1L, "치킨");
        Menu menu = new Menu(1L, "후라이드 치킨", BigDecimal.valueOf(1000), menuGroup);

        Product product = new Product(1L, "후라이드 치킨", BigDecimal.valueOf(1000));
        MenuProduct menuProduct = new MenuProduct(menu, product, 1);

        // when
        BigDecimal result = menuProduct.amount();

        // then
        assertThat(result).isEqualTo(BigDecimal.valueOf(1000));
    }
}
