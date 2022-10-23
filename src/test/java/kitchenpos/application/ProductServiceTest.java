package kitchenpos.application;

import static kitchenpos.application.exception.ExceptionType.INVALID_PRODUCT_PRICE_EXCEPTION;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ### Product &#xC0C1;&#xD488;&#xC740; `&#xC774;&#xB984;`&#xACFC; `&#xAC00;&#xACA9;`&#xC744; &#xAC00;&#xC9C0;&#xACE0;
 * &#xC788;&#xC2B5;&#xB2C8;&#xB2E4;.
 * <br>
 * - create (&#xC0C1;&#xD488;&#xC744; &#xC0DD;&#xC131;&#xD560; &#xC218; &#xC788;&#xC2B5;&#xB2C8;&#xB2E4;)
 * <br>
 * - price &#xAC00; &#xD544;&#xC218;&#xAC12;&#xC785;&#xB2C8;&#xB2E4;.
 * <br>
 * - price > 0 &#xC870;&#xAC74;&#xC744; &#xB9CC;&#xC871;&#xD574;&#xC57C;&#xD569;&#xB2C8;&#xB2E4;.
 * <br>
 * - findAll (&#xC0C1;&#xD488; &#xC804;&#xCCB4;&#xB97C; &#xC870;&#xD68C;&#xD569;&#xB2C8;&#xB2E4;.)
 */
class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    void 상품의_가격이_없을시_예외를_반환한다() {
        final Product 짜장면 = new Product(1L, "짜장면", BigDecimal.valueOf(-1));

        Assertions.assertThatThrownBy(() -> productService.create(짜장면))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INVALID_PRODUCT_PRICE_EXCEPTION.getMessage());
    }

    @Test
    void 상품의_가격이_null_일때_예외를_반환한다() {
        final Product 짜장면 = new Product(1L, "짜장면", null);

        Assertions.assertThatThrownBy(() -> productService.create(짜장면))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INVALID_PRODUCT_PRICE_EXCEPTION.getMessage());
    }
}
