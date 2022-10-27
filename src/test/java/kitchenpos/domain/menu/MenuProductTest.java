package kitchenpos.domain.menu;

import static kitchenpos.fixture.Fixture.PRODUCT_후라이드;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @Test
    void 가격을_계산한다() {
        final MenuProduct menuProduct = new MenuProduct(PRODUCT_후라이드, 5L);

        assertThat(menuProduct.calculatePrice()).isEqualByComparingTo(BigDecimal.valueOf(80000));
    }
}
