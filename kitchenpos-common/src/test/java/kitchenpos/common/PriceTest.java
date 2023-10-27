package kitchenpos.common;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PriceTest {

    @Test
    void 가격은_비워둘_수_없다() {
        BigDecimal nullAmount = null;

        assertThatThrownBy(() -> new Price(nullAmount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 비워둘 수 없습니다");
    }

    @Test
    void 가격은_0보다_작아선_안된다() {
        assertThatThrownBy(() -> new Price(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 0보다 작을 수 없습니다");
    }

    @Test
    void 가격은_0부터_가능하다() {
        assertThatNoException()
                .isThrownBy(() -> new Price(0));
    }

    @Test
    void 가격이_같은지_비교한다() {
        Price price = new Price("7777.3199");
        Price other = new Price("7777.3199");

        assertThat(price.isEqualTo(other)).isTrue();
    }

    @Test
    void 소수점_끝자리_0은_비교에_영향이_없다() {
        Price price = new Price("7777.3199");
        Price other = new Price("7777.319900000000000");

        assertThat(price.isEqualTo(other)).isTrue();
    }

    @Test
    void 가격이_더_작은지_비교한다() {
        Price price = new Price("7777.3199");
        Price other = new Price("7777.319900000000001");

        assertThat(price.isLessThan(other)).isTrue();
    }

    @Test
    void 가격이_더_큰지_비교한다() {
        Price price = new Price("7777.31990000002");
        Price other = new Price("7777.319900000000001");

        assertThat(price.isGreaterThan(other)).isTrue();
    }

    @Test
    void 가격끼리_더할_수_있다() {
        Price expected = new Price("15554.63");

        Price price = new Price("7777.3199");
        Price other = new Price("7777.3101");
        Price sum = price.add(other);

        assertThat(sum.isEqualTo(expected)).isTrue();
    }

    @Test
    void 가격을_곱할_수_있다() {
        Price expected = new Price("23331.9399");

        Price price = new Price("7777.3133");
        Price multiplied = price.multiply(3);

        assertThat(multiplied.isEqualTo(expected)).isTrue();
    }

    @Test
    void 가격에_0을_곱하면_0이된다() {
        Price expected = Price.ZERO;

        Price price = new Price("7777.3133");
        Price multiplied = price.multiply(0);

        assertThat(multiplied.isEqualTo(expected)).isTrue();
    }

    @Test
    void 가격에_음수를_곱할_수_없다() {
        Price price = new Price("7777.3133");

        assertThatThrownBy(() -> price.multiply(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격에 음수를 곱할 수 없습니다");
    }
}
