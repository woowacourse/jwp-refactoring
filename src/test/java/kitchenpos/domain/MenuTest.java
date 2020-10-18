package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @DisplayName("validateByPrice: 금액이 0 미만인 경우 예외처리")
    @Test
    void validateByPriceTest() {
        assertThatThrownBy(() -> new Menu("후라이드+후라이드", BigDecimal.valueOf(-1), 1L, new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("금액이 0미만일수 없습니다.");
    }
}
