package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceTest {

    @DisplayName("가격의 값은 0이상이다. 그렇지 않으면 예외가 발생한다.")
    @Test
    void constructor() {
        assertThatThrownBy(() -> new Price(-1L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
