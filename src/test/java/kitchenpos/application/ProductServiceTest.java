package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import kitchenpos.annotation.MockTest;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@MockTest
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @Test
    void 상품을_저장한다() {
        // given
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(1000));

        // when
        productService.create(product);

        // then
        verify(productDao).save(product);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 1000})
    void 상품의_가격이_0_이상이면_예외를_발생하지_않는다(int price) {
        // given
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(price));

        // then
        assertThatNoException().isThrownBy(
                () -> productService.create(product)
        );
    }

    @Test
    void 상품을_저장할_때_가격이_null이면_예외를_발생한다() {
        // given
        Product product = new Product();

        // then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품을_저장할_때_가격이_0_이하이면_예외가_발생한다() {
        // given
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(-1));

        // then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 모든_상품을_조회한다() {
        // when
        productService.list();

        // then
        verify(productDao).findAll();
    }
}
