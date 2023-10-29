package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceTest {

    @Test
    @DisplayName("메뉴 금액이 존재하지 않으면 예외가 발생한다")
    void create_fail() {
        assertThatThrownBy(() -> new Price(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("금액이 없거나 음수입니다.");
    }

    @Test
    @DisplayName("메뉴 금액이 음수이면 예외가 발생한다")
    void create_fail2() {
        assertThatThrownBy(() -> new Price(BigDecimal.valueOf(-2)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("금액이 없거나 음수입니다.");
    }
}
