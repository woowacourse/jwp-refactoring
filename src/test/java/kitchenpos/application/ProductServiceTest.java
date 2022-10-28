package kitchenpos.application;

import static kitchenpos.fixture.ProductFixture.getProduct;
import static kitchenpos.fixture.ProductFixture.getProductRequest;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class ProductServiceTest extends ServiceTest{

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("싱품의 가격이 0일 경우 상품이 생성되어야 한다.")
    void createWithValidPrice() {
        // given
        final Product requestProduct = getProductRequest(0);

        // when
        when(productDao.save(any())).thenReturn(getProduct(1L, 17000));

        // then
        assertDoesNotThrow(() -> productService.create(requestProduct));
    }

    @ParameterizedTest(name = "상품의 가격이 음수({0})일 경우 예외가 발생한다.")
    @ValueSource(ints = {-100, -1})
    void createWithNegativePrice(final int price) {
        final Product productRequest = getProductRequest(price);
        assertThatThrownBy(() -> productService.create(productRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품의 가격이 null 일 경우 예외가 발생한다.")
    void createWithNullPrice() {
        final Product productRequest = getProductRequest(null);
        assertThatThrownBy(() -> productService.create(productRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
