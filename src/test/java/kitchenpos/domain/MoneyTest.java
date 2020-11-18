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
}