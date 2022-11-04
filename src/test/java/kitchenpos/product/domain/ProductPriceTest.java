package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ProductPriceTest {

    @Test
    void 생성_시_null이면_예외가_발생한다() {
        assertThatThrownBy(() -> new ProductPrice(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 null 혹은 0 미만일 수 없습니다.");
    }

    @Test
    void 생성_시_음수이면_예외가_발생한다() {
        assertThatThrownBy(() -> new ProductPrice(BigDecimal.valueOf(-1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 null 혹은 0 미만일 수 없습니다.");
    }
}
