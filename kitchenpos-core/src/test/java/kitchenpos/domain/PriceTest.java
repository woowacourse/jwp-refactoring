package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import kitchenpos.common.UnitTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@UnitTest
class PriceTest {

    @Test
    void price를_생성한다() {
        BigDecimal price = BigDecimal.valueOf(16000);

        Price actual = new Price(price);

        assertThat(actual).isEqualTo(new Price(BigDecimal.valueOf(16000)));
    }

    @ParameterizedTest(name = "price가 {0}미만인 경우 예외를 던진다")
    @ValueSource(ints = {-15000, -10, Integer.MIN_VALUE})
    void price가_0미만인_경우_예외를_던진다(final int price) {
        assertThatThrownBy(() -> new Price(BigDecimal.valueOf(price)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void price가_특정_price보다_큰_경우_예외를_던진다() {
        BigDecimal price = BigDecimal.valueOf(16000);

        assertThatThrownBy(() -> new Price(price).validateGreaterThan(BigDecimal.valueOf(15000)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void price와_특정_값을_곱연산_한다() {
        Price price = new Price(BigDecimal.valueOf(16000));

        BigDecimal actual = price.multiply(BigDecimal.valueOf(3));

        assertThat(actual.compareTo(BigDecimal.valueOf(48000))).isEqualTo(0);
    }
}
