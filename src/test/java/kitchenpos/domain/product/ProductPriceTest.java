package kitchenpos.domain.product;

import org.junit.jupiter.api.Test;

import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductPriceTest {


    @Test
    void 상품_가격은_null일_수_없다() {
        assertThatThrownBy(() -> new Product(null, "후라이드", null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_가격은_음수일_수_없다() {
        assertThatThrownBy(() -> new Product(null, "후라이드", valueOf(-16000)))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
