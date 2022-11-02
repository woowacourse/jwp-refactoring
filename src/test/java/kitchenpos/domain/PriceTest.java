package kitchenpos.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PriceTest {

    @DisplayName("null로 생성 시 예외를 발생시킨다.")
    @Test
    void test() {
        assertThatThrownBy(() -> new Price(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("0보다 작은 값으로 생성 시 예외를 발생시킨다.")
    @Test
    void lessThanZero_exception() {
        assertThatThrownBy(() -> new Price(-1L)).isInstanceOf(IllegalArgumentException.class);
    }
}
