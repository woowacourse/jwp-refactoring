package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MoneyTest {

    @DisplayName("Money 더하기")
    @Test
    void plus() {
        Money money = new Money(1000L);
        assertThat(money.plus(new Money(1000L)).getValue()).isEqualTo(2000L);
    }

    @DisplayName("Money 비교")
    @Test
    void compareTo() {
        Money money = new Money(2000L);
        Money targetMoney = new Money(1000L);
        assertThat(money.compareTo(targetMoney)).isEqualTo(1000L);
    }

    @DisplayName("Money의 값이 0 일 때 - 값이 음수인지 확인")
    @Test
    void isMinus_whenMoneyValueIsZero() {
        Money money = new Money(0L);

        assertThat(money.isMinus()).isEqualTo(false);
    }

    @DisplayName("Money의 값이 음수일 때 - 값이 음수인지 확인")
    @Test
    void isMinus_whenMoneyValueIsMinus() {
        Money money = new Money(-1L);

        assertThat(money.isMinus()).isEqualTo(true);
    }
}