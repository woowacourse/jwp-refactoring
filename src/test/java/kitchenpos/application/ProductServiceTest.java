package kitchenpos.application;

import static kitchenpos.application.exception.ExceptionType.INVALID_PRODUCT_PRICE_EXCEPTION;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ### Product 상품은 `이름`과 `가격`을 가지고 있습니다.
 * <br>
 * - create (상품을 생성할 수 있습니다)
 * <br>
 * - price 가 필수값입니다.
 * <br>
 * - price > 0 조건을 만족해야합니다.
 * <br>
 * - findAll (상품 전체를 조회합니다.)
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
