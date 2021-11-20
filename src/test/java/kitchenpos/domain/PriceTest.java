package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.exception.InvalidPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Price 단위 테스트")
class PriceTest {

    @DisplayName("Price를 생성할 때")
    @Nested
    class Create {

        @DisplayName("value가 Null일 경우 예외가 발생한다.")
        @Test
        void valueNullException() {
            // when, then
            assertThatThrownBy(() -> new Price(null))
                .isExactlyInstanceOf(InvalidPriceException.class);
        }

        @DisplayName("value가 0보다 작을 경우 예외가 발생한다.")
        @Test
        void valueNegativeException() {
            // when, then
            assertThatThrownBy(() -> new Price(BigDecimal.valueOf(-1)))
                .isExactlyInstanceOf(InvalidPriceException.class);
        }
    }

    @DisplayName("금액이 더 큰지 비교가 가능하다")
    @Test
    void isBiggerThan() {
        // given
        Price price = new Price(BigDecimal.valueOf(3_000));
        Price smallerPrice = new Price(BigDecimal.valueOf(2_000));
        Price equalPrice = new Price(BigDecimal.valueOf(3_000));
        Price biggerPrice = new Price(BigDecimal.valueOf(5_000));

        // when, then
        assertThat(price.isBiggerThan(smallerPrice)).isTrue();
        assertThat(price.isBiggerThan(equalPrice)).isFalse();
        assertThat(price.isBiggerThan(biggerPrice)).isFalse();
    }

    @DisplayName("주어진 수량에 곱해진 가격의 반환한다.")
    @Test
    void multiplyQuantity() {
        // given
        BigDecimal priceValue = BigDecimal.valueOf(3_000);
        Price price = new Price(priceValue);

        // when
        Quantity quantity = new Quantity(4L);
        Price result = price.multiplyQuantity(quantity);

        // then
        assertThat(result.getValue()).isEqualTo(priceValue.multiply(quantity.getDecimalValue()));
    }
}
