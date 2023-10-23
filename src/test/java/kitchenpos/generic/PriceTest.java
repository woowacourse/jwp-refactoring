package kitchenpos.generic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
class PriceTest {

    @Test
    void throw_when_price_is_under_or_equal_than_zero() {
        // when & then
        assertThatThrownBy(() -> Price.from(-1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Price must be greater than zero.");
    }

    @Test
    void multiply() {
        // given
        final Price price = Price.from(1000L);

        // when
        final Price multipliedPrice = price.multiply(2L);

        // then
        assertThat(multipliedPrice).isEqualTo(Price.from(2000L));
    }

    @Test
    void add() {
        // given
        final Price price = Price.from(1000L);

        // when
        final Price addedPrice = price.add(Price.from(2000L));

        // then
        assertThat(addedPrice).isEqualTo(Price.from(3000L));
    }

    @Test
    void is_greater_than_other_price() {
        // given
        final Price price = Price.from(1000L);

        // when
        final boolean isGreaterThan = price.isGreaterThan(Price.from(999L));

        // then
        assertThat(isGreaterThan).isTrue();
    }

    @Test
    void is_greater_than_method_compare_exclusively() {
        // given
        final Price price = Price.from(1000L);

        // when
        final boolean isGreaterThan = price.isGreaterThan(Price.from(1000L));

        // then
        assertThat(isGreaterThan).isFalse();
    }
}
