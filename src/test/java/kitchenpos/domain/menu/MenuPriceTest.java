package kitchenpos.domain.menu;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class MenuPriceTest {
    
    @Test
    @DisplayName("가격이 null이거나 음수이면 예외가 발생한다.")
    void validateMenuPrice() {
        // given
        final BigDecimal price1 = null;
        final BigDecimal price2 = BigDecimal.valueOf(-1);

        // when, then
        assertSoftly(softly -> {
            assertThatThrownBy(() -> new MenuPrice(price1)).isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> new MenuPrice(price2)).isInstanceOf(IllegalArgumentException.class);
        });
    }
}
