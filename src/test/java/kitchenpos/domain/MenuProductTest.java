package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuProductTest {

    @Test
    @DisplayName("상품이 존재하지 않으면 예외가 발생한다")
    void create_fail() {
        assertThatThrownBy(() -> new MenuProduct(null, 4))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품이 존재해야 합니다.");
    }

    @Test
    @DisplayName("수량이 1개 미만이면 예외가 발생한다")
    void create_fail2() {
        assertThatThrownBy(() -> new MenuProduct(1L, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품의 갯수는 양수여야 합니다.");
    }
}
