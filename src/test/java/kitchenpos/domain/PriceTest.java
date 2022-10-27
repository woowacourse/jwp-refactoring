package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PriceTest {

    @DisplayName("가격의 값은 0이상이다. 그렇지 않으면 예외가 발생한다.")
    @Test
    void constructor() {
        assertThatThrownBy(() -> new Price(-1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Price의 가격이 더 비싸면 True, 이외는 false를 반환한다.")
    @ParameterizedTest
    @CsvSource({"999,true", "1000,false"})
    void isExpensiveThan(long other, boolean expect) {
        Price price = new Price(1000L);

        boolean result = price.isExpensiveThan(new Price(other));

        assertThat(result).isEqualTo(expect);
    }
}
