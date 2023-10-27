package kitchenpos.product.domain;

import kitchenpos.BaseTest;
import kitchenpos.product.domain.ProductPrice;
import kitchenpos.product.exception.ProductPriceException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class ProductPriceTest extends BaseTest {

    @Test
    void 상품_가격을_생성한다() {
        // given
        BigDecimal price = BigDecimal.valueOf(1000L);

        // when, then
        Assertions.assertThatNoException()
                .isThrownBy(() -> new ProductPrice(price));
    }

    @Test
    void 상품_가격이_null_이면_예외를_던진다() {
        // given
        BigDecimal price = null;

        // when, then
        Assertions.assertThatThrownBy(() -> new ProductPrice(price))
                .isInstanceOf(ProductPriceException.class);
    }

    @Test
    void 상품_가격이_음수_이면_예외를_던진다() {
        // given
        BigDecimal price = BigDecimal.valueOf(-1000L);

        // when, then
        Assertions.assertThatThrownBy(() -> new ProductPrice(price))
                .isInstanceOf(ProductPriceException.class);
    }
}
