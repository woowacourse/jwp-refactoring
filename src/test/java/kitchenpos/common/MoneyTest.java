package kitchenpos.common;

import kitchenpos.common.Money;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MoneyTest {

    @Test
    void 음수로_생성하면_예외가_발생한다() {
        //expect
        assertThatThrownBy(() -> Money.of(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("가격이 없거나 0보다 작습니다.");
    }

    @Test
    void 값이_같으면_동등하다() {
        //given
        Money money = Money.of(1000);
        Money other = Money.of(1000);

        //when
        boolean actual = money.equals(other);

        //then
        assertThat(actual).isTrue();
    }

    @Test
    void 값을_더할_수_있다() {
        //given
        Money money = Money.of(1000);

        //when
        Money actual = money.plus(Money.of(1000));

        //then
        assertThat(actual).isEqualTo(Money.of(2000));
    }

    @Test
    void 값을_곱할_수_있다() {
        //given
        Money money = Money.of(1000);

        //when
        Money actual = money.times(2);

        //then
        assertThat(actual).isEqualTo(Money.of(2000));
    }

    @Test
    void 값을_비교할_수_있다() {
        //given
        Money money = Money.of(1000);
        Money other = Money.of(2000);

        //when
        boolean actual = money.isHigherThan(other);

        //then
        assertThat(actual).isFalse();
    }
}
