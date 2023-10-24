package kitchenpos.domain;

import kitchenpos.domain.vo.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class PriceTest {

    @DisplayName("양수의 가격이면 객체 생성에 성공한다.")
    @Test
    void construct_success_when_positive_value() {
        // given
        final BigDecimal value = BigDecimal.valueOf(2000);

        // then
        assertThatCode(() -> new Price(value))
                .doesNotThrowAnyException();
    }

    @DisplayName("음의 가격을 입력하면 객체 생성에 실패한다.")
    @Test
    void construct_fail_when_negative_value() {
        // given
        final BigDecimal value = BigDecimal.valueOf(-1000);

        // then
        assertThatThrownBy(() -> new Price(value))
                .isInstanceOfAny(IllegalArgumentException.class);
    }

    @DisplayName("초기 가격은 0원 이다.")
    @Test
    void initial_value_is_zero() {
        // given
        final Price expected = new Price(BigDecimal.ZERO);

        // when
        final Price actual = Price.init();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("1000원에 2를 곱하면 2000원이다.")
    @Test
    void multiply_success() {
        // given
        final Price price = new Price(BigDecimal.valueOf(1000));
        final long multiplier = 2L;

        // when
        final Price expected = new Price(BigDecimal.valueOf(2000));
        final Price actual = price.multiply(multiplier);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("1000원에 2000원을 더하면 3000원이다.")
    @Test
    void addition_success() {
        // given
        final Price price = new Price(BigDecimal.valueOf(1000));
        final Price additionalPrice = new Price(BigDecimal.valueOf(2000));

        // when
        final Price expected = new Price(BigDecimal.valueOf(3000));
        final Price actual = price.add(additionalPrice);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("1000원은 1000원과 2000원보다 크지 않고, 500원 보다는 크다.")
    @Test
    void biggerThan_success() {
        // given
        final Price price = new Price(BigDecimal.valueOf(1000));
        final Price biggerComparationPrice = new Price(BigDecimal.valueOf(2000));
        final Price sameComparationPrice = new Price(BigDecimal.valueOf(1000));
        final Price smallerComparationPrice = new Price(BigDecimal.valueOf(500));

        // when
        final boolean biggerResult = price.biggerThan(biggerComparationPrice);
        final boolean sameResult = price.biggerThan(sameComparationPrice);
        final boolean smallerResult = price.biggerThan(smallerComparationPrice);

        // then
        assertSoftly(softly -> {
            assertThat(biggerResult).isFalse();
            assertThat(sameResult).isFalse();
            assertThat(smallerResult).isTrue();
        });
    }
}
