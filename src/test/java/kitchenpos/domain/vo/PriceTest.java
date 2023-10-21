package kitchenpos.domain.vo;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("가격 테스트")
class PriceTest {

    @DisplayName("가격이 없으면 예외처리 한다")
    @Test
    void throwExceptionWhenPriceIsNull() {
        assertThatThrownBy(() -> new Price(null))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("올바르지 않은 가격입니다.");
    }

    @DisplayName("가격이 0 미만이면 예외처리 한다")
    @ParameterizedTest
    @ValueSource(ints = {-10000, -1})
    void throwExceptionWhenPriceIsLowerThanZero(int price) {
        assertThatThrownBy(() -> new Price(BigDecimal.valueOf(price)))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("올바르지 않은 가격입니다.");
    }
}
