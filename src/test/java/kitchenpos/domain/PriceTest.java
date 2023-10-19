package kitchenpos.domain;

import static kitchenpos.domain.exception.PriceExceptionType.PRICE_IS_LOWER_THAN_ZERO;
import static kitchenpos.domain.exception.PriceExceptionType.PRICE_IS_NULL;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import kitchenpos.domain.exception.PriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PriceTest {

    @Test
    @DisplayName("price는 0보다 작을 수 업습니다.")
    void throwExceptionValueIsLowerThan0() {
        final BigDecimal value = BigDecimal.valueOf(-1);

        assertThatThrownBy(() -> new Price(value))
            .isInstanceOf(PriceException.class)
            .hasMessage(PRICE_IS_LOWER_THAN_ZERO.getMessage());
    }

    @Test
    @DisplayName("price는 null일 수 없습니다.")
    void throwExceptionValueIsNull() {
        assertThatThrownBy(() -> new Price(null))
            .isInstanceOf(PriceException.class)
            .hasMessage(PRICE_IS_NULL.getMessage());
    }

    @Nested
    @DisplayName("price의 대소비교를 한다.")
    class IsBigger {

        @Test
        @DisplayName("파라미터가 더 작은 경우")
        void tureCase() {
            final Price price = new Price(BigDecimal.valueOf(1000));
            final Price smaller = new Price(BigDecimal.valueOf(500));

            assertTrue(price.isBigger(smaller));
        }

        @Test
        @DisplayName("파라미터가 더 큰 경우")
        void falseCase() {
            final Price price = new Price(BigDecimal.valueOf(1000));
            final Price bigger = new Price(BigDecimal.valueOf(2000));

            assertFalse(price.isBigger(bigger));
        }
    }

}
