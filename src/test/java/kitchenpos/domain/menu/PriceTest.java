package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.domain.common.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PriceTest {

    @DisplayName("가격 생성자")
    @Nested
    class Constructor {

        @DisplayName("가격이 0보다 작다면, IAE를 던진다.")
        @ValueSource(ints = {-1, -100, -1000})
        @ParameterizedTest
        void Should_ThrowIAE_When_PriceIsLessThan0(final int price) {
            // given & when & then
            assertThatThrownBy(() -> new Price(BigDecimal.valueOf(price)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
