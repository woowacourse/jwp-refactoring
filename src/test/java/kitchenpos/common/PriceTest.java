package kitchenpos.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceTest {

    @Test
    @DisplayName("가격 객체를 정상적으로 생성한다.")
    void createPrice() {
        // when
        final Price price = new Price(new BigDecimal("1000.00"));

        // then
        assertAll(
                () -> assertThat(price.getValue()).isEqualTo("1000.00")
        );
    }

    @Test
    @DisplayName("가격이 음수인 경우 예외가 발생한다.")
    void throwsExceptionWhenPriceIsUnderZero() {
        // when, then
        assertThatThrownBy(() -> new Price(new BigDecimal("-1000.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 0보다 작을 수 없습니다.");
    }
}
