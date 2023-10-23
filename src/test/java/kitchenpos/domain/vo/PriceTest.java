package kitchenpos.domain.vo;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class PriceTest {

    @Test
    void 가격이_0미만_이면_예외가_발생한다() {
        // given
        final BigDecimal value = BigDecimal.valueOf(-1);

        // expected
        assertThatThrownBy(() -> new Price(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("가격은 0이상 이어야 합니다.");
    }
}
