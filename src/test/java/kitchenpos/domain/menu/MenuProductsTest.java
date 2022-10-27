package kitchenpos.domain.menu;

import static kitchenpos.fixture.Fixture.PRODUCT_양념치킨;
import static kitchenpos.fixture.Fixture.PRODUCT_후라이드;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class MenuProductsTest {

    @Test
    void 전체_가격을_계산한다() {
        final MenuProduct menuProduct1 = new MenuProduct(PRODUCT_후라이드, 1L);
        final MenuProduct menuProduct2 = new MenuProduct(PRODUCT_양념치킨, 1L);
        final Menu menu = new Menu("후라이드+양념", new MenuPrice(BigDecimal.valueOf(32000)), 1L,
                List.of(menuProduct1, menuProduct2));

        assertThat(menu.getMenuProducts().calculateEntirePrice()).isEqualByComparingTo(BigDecimal.valueOf(33000));
    }
}
