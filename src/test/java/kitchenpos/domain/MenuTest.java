package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    @DisplayName("[EXCEPTION] 메뉴 가격이 null 일 경우 예외가 발생한다.")
    @Test
    void throwException_price_isNull() {
        // expect
        assertThatThrownBy(() -> new Menu("테스트용 메뉴명", null, null, Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[EXCEPTION] 등록할 신규 메뉴에 가격이 음수일 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"-1", "-10", "-100", "-1000000"})
    void throwException_price_isNegative(final String negativePriceValue) {
        // expect
        assertThatThrownBy(() -> new Menu("테스트용 메뉴명", new BigDecimal(negativePriceValue), null, Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
