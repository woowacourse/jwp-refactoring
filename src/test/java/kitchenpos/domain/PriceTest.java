package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceTest {

    @DisplayName("가격은 비어있을 수 없다.")
    @Test
    void create_null_fail() {
        // given
        // when, then
        assertThatThrownBy(() -> Price.from(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("가격은 비어있을 수 없습니다.");
    }

    @DisplayName("가격은 0원 이상이어야 한다.")
    @Test
    void create_negative_fail() {
        // given
        // when, then
        assertThatThrownBy(() -> Price.from(BigDecimal.valueOf(-1L)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("가격은 0원 이상이어야 합니다.");
    }

    @DisplayName("가격을 생성한다.")
    @Test
    void create() {
        // given
        // when, then
        assertDoesNotThrow(() -> Price.from(BigDecimal.valueOf(1000L)));
    }
}
