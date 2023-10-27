package kitchenpos.domain;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuValidator;
import kitchenpos.domain.menu.MenuGroup;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuValidatorTest {

    @Test
    void 메뉴는_0_이상의_가격을_가진다() {
        final Menu menu = new Menu("스키야키", BigDecimal.valueOf(-1), new MenuGroup("일식"), new ArrayList<>());

        assertThatThrownBy(() -> new MenuValidator().validate(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
