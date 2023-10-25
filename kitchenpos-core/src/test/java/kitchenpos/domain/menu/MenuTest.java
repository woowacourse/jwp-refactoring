package kitchenpos.domain.menu;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    @Test
    void 메뉴의_가격은_0원_보다_작으면_예외가_발생한다() {
        // given
        BigDecimal price = BigDecimal.valueOf(-1);

        // expect
        assertThatThrownBy(() -> new Menu("menu", price, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 0원 이상이여야합니다");
    }
}
