package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    @Test
    void 메뉴는_0_이상의_가격을_가진다() {
        assertThatThrownBy(() -> new Menu("스키야키", BigDecimal.valueOf(-1), 2L, new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
