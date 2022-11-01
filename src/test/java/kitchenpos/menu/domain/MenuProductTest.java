package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @Test
    @DisplayName("예외사항이 존재하지 않는 경우 객체를 생성한다.")
    void order() {
        assertDoesNotThrow(() -> new MenuProduct(null, 1L, 1L));
    }

    @Test
    @DisplayName("메뉴 수량이 1미만인 경우 예외가 발생한다.")
    void lessThanOne() {
        assertThatThrownBy(() -> new MenuProduct(null, 1L, 0L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("메뉴 수량은 1이상이어야 합니다.");
    }

}
