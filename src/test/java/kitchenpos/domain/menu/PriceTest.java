package kitchenpos.domain.menu;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PriceTest {
    @DisplayName("가격이 0원 미만이면 예외가 발생한다.")
    @Test
    void createExceptionTest_priceIsZero() {
        assertThatThrownBy(() -> new Price(-1L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
