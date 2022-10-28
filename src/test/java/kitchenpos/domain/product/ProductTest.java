package kitchenpos.domain.product;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void 상품의_가격은_음수일_수_없다() {
        assertThatThrownBy(() -> new Product("제품1", new BigDecimal(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
