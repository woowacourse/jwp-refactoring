package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class PriceTest {

    @Test
    void 가격이_0원_미만이면_예외가_발생한다() {
        // when, then
        assertThatThrownBy(() -> new Price(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 가격을_비교_할_수_있다() {
        // given
        Price priceA = new Price(1000);
        Price priceB = new Price(500);
        Price priceC = new Price(1000);

        // when
        boolean resultA = priceA.isGreaterThan(priceB);
        boolean resultB = priceA.isGreaterThan(priceC);
        boolean resultC = priceB.isGreaterThan(priceA);

        // then
        assertThat(resultA).isTrue();
        assertThat(resultB).isFalse();
        assertThat(resultC).isFalse();
    }

    @Test
    void 가격을_더할_수_있다() {
        // given
        Price priceA = new Price(1000);
        Price priceB = new Price(500);

        // when
        Price result = priceA.add(priceB);

        // then
        assertThat(result).isEqualTo(new Price(1500));
    }

    @Test
    void 가격을_수량으로_곱할_수_있다() {
        // given
        Price price = new Price(1000);

        // when
        Price result = price.multiply(2L);

        // then
        assertThat(result).isEqualTo(new Price(2000));
    }

}
