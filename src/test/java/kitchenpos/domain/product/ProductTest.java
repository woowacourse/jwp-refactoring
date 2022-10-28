package kitchenpos.domain.product;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ProductTest {
    @Test
    void 상품을_생성할_수_있다() {
        assertDoesNotThrow(() -> new Product("제품1", new BigDecimal(1000)));
    }

    @Test
    void 상품의_가격은_음수일_수_없다() {
        assertThatThrownBy(() -> new Product("제품1", new BigDecimal(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
