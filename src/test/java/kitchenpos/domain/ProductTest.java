package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.exception.ProductPriceException;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void 가격이_유효하지_않으면_예외를_발생한다() {
        assertThatThrownBy(() -> new Product("", BigDecimal.valueOf(-1L)))
                .isInstanceOf(ProductPriceException.class);
        assertThatThrownBy(() -> new Product("", null))
                .isInstanceOf(ProductPriceException.class);
    }
}
