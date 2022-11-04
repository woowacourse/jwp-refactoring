package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MenuPriceTest {

    @Test
    void 생성_시_null이면_예외가_발생한다() {
        assertThatThrownBy(() -> new MenuPrice(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 null 혹은 0 미만일 수 없습니다.");
    }

    @Test
    void 생성_시_음수이면_예외가_발생한다() {
        assertThatThrownBy(() -> new MenuPrice(BigDecimal.valueOf(-1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 null 혹은 0 미만일 수 없습니다.");
    }

    @Test
    void 입력받은_가격보다_비싸면_참을_반환한다() {
        final MenuPrice menuPrice = new MenuPrice(BigDecimal.valueOf(1000));

        assertThat(menuPrice.isExpensive(BigDecimal.valueOf(999))).isTrue();
    }

    @ParameterizedTest
    @ValueSource(longs = {1000L, 1001L})
    void 입력받은_가격과_같거나_저렴하면_거짓을_반환한다(final long price) {
        final MenuPrice menuPrice = new MenuPrice(BigDecimal.valueOf(1000));

        assertThat(menuPrice.isExpensive(BigDecimal.valueOf(price))).isFalse();
    }
}
