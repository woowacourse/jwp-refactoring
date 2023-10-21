package kitchenpos.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    @Test
    void 메뉴는_0_이상의_가격을_가진다() {
        final Menu menu = new Menu();
        menu.setName("스키야키");
        assertThatThrownBy(() -> menu.setPrice(BigDecimal.valueOf(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
