package kitchenpos.application;

import static kitchenpos.exception.ProductExceptionType.PRICE_IS_NULL_OR_MINUS_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import kitchenpos.common.IntegrationTest;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.ProductCreateRequest;
import kitchenpos.exception.ProductException;
import kitchenpos.exception.ProductExceptionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class ProductServiceTest extends IntegrationTest {

    @Autowired
    private ProductService productService;

    @Test
    void 상품을_저장한다() {
        // given
        ProductCreateRequest productCreateRequest = new ProductCreateRequest("신상품", 1000);

        // expect
        assertThatNoException().isThrownBy(
                () -> productService.create(productCreateRequest)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 1000})
    void 상품의_가격이_0_이상이면_예외를_발생하지_않는다(int price) {
        // given
        ProductCreateRequest productCreateRequest = new ProductCreateRequest("신상품", price);

        // then
        assertThatNoException().isThrownBy(
                () -> productService.create(productCreateRequest)
        );
    }

    @Test
    void 상품을_저장할_때_가격이_0_이하이면_예외가_발생한다() {
        // given
        ProductCreateRequest productCreateRequest = new ProductCreateRequest("신상품", -1);

        // when
        ProductExceptionType exceptionType = assertThrows(ProductException.class,
                () -> productService.create(productCreateRequest)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(PRICE_IS_NULL_OR_MINUS_EXCEPTION);
    }

    @Test
    void 모든_상품을_조회한다() {
        // when
        List<Product> products = productService.list();

        // then
        assertThat(products).isNotNull();
        assertThat(products.size()).isGreaterThanOrEqualTo(0);
    }
}
