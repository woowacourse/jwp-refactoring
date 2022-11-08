package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {
    @Test
    @DisplayName("가격이 음수인 경우 메뉴를 생성할 수 없다")
    void createMenu() {
        assertThatThrownBy(() -> new Menu("케이메뉴", BigDecimal.valueOf(-1L), 1L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
