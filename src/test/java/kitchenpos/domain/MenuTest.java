package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.exception.PriceException;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    void 가격이_유효하지_않으면_예외를_발생한다() {
        assertThatThrownBy(() -> new Menu("", BigDecimal.valueOf(-1L), 1L))
                .isInstanceOf(PriceException.class);
        assertThatThrownBy(() -> new Menu("", null, 1L))
                .isInstanceOf(PriceException.class);
    }
}
