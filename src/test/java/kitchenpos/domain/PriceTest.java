package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.order.domain.Price;
import org.junit.jupiter.api.Test;

class PriceTest {

    @Test
    void 가격이_음수면_예외가_발생한다() {
        assertThatThrownBy(() ->
                new Price(
                        BigDecimal.valueOf(-1))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 가격이_Null_이면_예외가_발생한다() {
        assertThatThrownBy(() ->
                new Price(null)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
