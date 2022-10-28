package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void 생성_시_null인_가격인_경우_예외가_발생한다() {
        assertThatThrownBy(() -> new Product("후라이드", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 양의 정수만 들어올 수 있습니다.");
    }

    @Test
    void 생성_시_음수인_가격인_경우_예외가_발생한다() {
        assertThatThrownBy(() -> new Product("후라이드", BigDecimal.valueOf(-1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 양의 정수만 들어올 수 있습니다.");
    }
}
