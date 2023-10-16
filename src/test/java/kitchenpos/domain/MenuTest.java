package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
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

    @DisplayName("[SUCCESS] 메뉴 가격이 입력된 가격보다 큰지 확인한다.")
    @ParameterizedTest
    @CsvSource(value = {"10000:9999:true", "10000:1:true", "10000:-10:true", "10000:10001:false", "10000:99999999:false"}, delimiter = ':')
    void isGreaterThan(final String menuPrice, final String otherPrice, final boolean expected) {
        // given
        final Menu menu = new Menu("테스트용 메뉴명", new BigDecimal(menuPrice), null, Collections.emptyList());

        // when
        final boolean actual = menu.isGreaterThan(new BigDecimal(otherPrice));

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
