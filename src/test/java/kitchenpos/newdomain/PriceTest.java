package kitchenpos.newdomain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.exception.CustomErrorCode;
import kitchenpos.exception.DomainLogicException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PriceTest {

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -1})
    void 가격이_0보다_작은_경우_예외를_던진다(final int price) {
        // when & then
        assertThatThrownBy(() -> Price.valueOf(price))
                .isInstanceOf(DomainLogicException.class)
                .extracting("errorCode")
                .isEqualTo(CustomErrorCode.PRICE_MIN_VALUE_ERROR);
    }

    @Test
    void 가격은_0일_수_있다() {
        // given
        final var value = 0;

        // when
        final var actual = Price.valueOf(value);

        // then
        assertThat(actual.getValue()).isEqualByComparingTo(BigDecimal.valueOf(value));
    }
}
