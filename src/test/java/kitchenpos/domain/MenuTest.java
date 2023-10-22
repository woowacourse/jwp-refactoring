package kitchenpos.domain;

import kitchenpos.fixture.MenuGroupFixture;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    @Test
    void 메뉴는_0_이상의_가격을_가진다() {
        assertThatThrownBy(() -> new Menu("스키야키", BigDecimal.valueOf(-1), MenuGroupFixture.일식(), new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
