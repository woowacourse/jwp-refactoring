package kitchenpos.domain.vo;

import kitchenpos.domain.exception.InvalidPriceValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("가격 테스트")
class PriceTest {

    @ParameterizedTest
    @ValueSource(ints = {0, 1, Integer.MAX_VALUE})
    void 가격을_생성한다(final Integer priceValue) {
        // given
        final BigDecimal value = BigDecimal.valueOf(priceValue);

        // when
        final Price price = new Price(value);

        // then
        assertThat(price.getValue()).isEqualTo(value);
    }

    @Test
    void 가격은_음수가_아닌_경우_예외를_반환한다() {
        // given
        final BigDecimal value = BigDecimal.valueOf(-1);

        // when & then
        assertThatThrownBy(() -> new Price(value))
                .isInstanceOf(InvalidPriceValue.class)
                .hasMessage("상품의 가격은 0 혹은 양수여야 합니다.");
    }

    @ParameterizedTest
    @NullSource
    void 가격은_null인_경우_예외를_반환한다(final BigDecimal priceValue) {
        // when & then
        assertThatThrownBy(() -> new Price(priceValue))
                .isInstanceOf(InvalidPriceValue.class)
                .hasMessage("상품의 가격은 0 혹은 양수여야 합니다.");
    }
}
