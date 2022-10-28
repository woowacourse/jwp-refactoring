package kitchenpos.domain.menu;

import static kitchenpos.fixture.Fixture.MENU_양념치킨;
import static kitchenpos.fixture.Fixture.MENU_후라이드치킨;
import static kitchenpos.fixture.Fixture.PRODUCT_양념치킨;
import static kitchenpos.fixture.Fixture.PRODUCT_후라이드;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class MenuProductsTest {

    @Test
    void 전체_가격을_계산한다() {
        final MenuProduct menuProduct1 = new MenuProduct(MENU_후라이드치킨, PRODUCT_후라이드, 1L);
        final MenuProduct menuProduct2 = new MenuProduct(MENU_양념치킨, PRODUCT_양념치킨, 1L);

        final MenuProducts menuProducts = new MenuProducts(List.of(menuProduct1, menuProduct2));

        assertThat(menuProducts.calculateEntirePrice()).isEqualByComparingTo(BigDecimal.valueOf(33000));
    }
}
