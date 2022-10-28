package kitchenpos.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    void validatePrice() {
        assertThatThrownBy(() -> new Menu(null, "name", BigDecimal.valueOf(-100), 1L, new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("product의 가격은 음수가 될 수 없습니다.");
    }
}
