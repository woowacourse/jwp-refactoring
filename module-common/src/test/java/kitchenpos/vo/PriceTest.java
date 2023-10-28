package kitchenpos.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.Test;

class PriceTest {

    @Test
    void 값은_음수가_될_수_없다() {
        // given
        long value = -1;

        // when
        // then
        assertThatIllegalArgumentException().isThrownBy(
                () -> new Price(value)
        );
    }

    @Test
    void 대소_비교를_한다() {
        // given
        Price one = new Price(1L);
        Price two = new Price(2L);
        Price zero = new Price(0L);

        // when
        boolean result1 = one.isBiggerThan(two);
        boolean result2 = one.isBiggerThan(zero);

        // then
        assertThat(result1).isFalse();
        assertThat(result2).isTrue();
    }

    @Test
    void 수량을_받아_총합을_계산한다() {
        // given
        Price price = new Price(1L);
        Quantity quantity = new Quantity(10);

        // when
        Price result = price.calculateAmount(quantity);

        // then
        assertThat(result).isEqualTo(new Price(10L));
    }

    @Test
    void 다른_Price와의_합을_계산한다() {
        // given
        Price price = new Price(10L);
        Price other = new Price(10L);

        // when
        Price result = price.add(other);

        // then
        assertThat(result).isEqualTo(new Price(20L));
    }

    @Test
    void 값이_같으면_동등한_객체이다() {
        // given
        Price price = new Price(10L);
        Price other = new Price(10L);

        // when
        boolean result = price.equals(other);

        // then
        assertThat(result).isTrue();
    }
}
