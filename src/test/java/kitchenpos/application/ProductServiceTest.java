package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.application.product.ProductService;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.ProductRequest;

class ProductServiceTest extends BaseServiceTest {

    @Autowired
    private ProductService productService;

    @Nested
    @DisplayName("Product 생성 테스트")
    class CreateProductTest {

        @Test
        @DisplayName("프로덕트를 생성한다.")
        void createProduct() {
            // Given
            final ProductRequest request = new ProductRequest("떡볶이", BigDecimal.ONE);

            // When
            Product createdProduct = productService.create(request);


            // Then
            assertSoftly(softAssertions -> {
                assertThat(createdProduct.getId()).isNotNull();
                assertThat(createdProduct.getName()).isEqualTo(request.getName());
                assertThat(createdProduct.getPrice()).isEqualByComparingTo(request.getPrice());
            });
        }

        @Test
        @DisplayName("가격이 0미만인 경우 예외가 발생한다.")
        void createProductWithSubZeroPrice() {
            // Given
            final ProductRequest request = new ProductRequest("떡볶이", BigDecimal.valueOf(-10));

            // When & Then
            assertThatThrownBy(() -> productService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("가격이 null인 경우 예외가 발생한다.")
        void createProductWithNullPrice() {
            // Given
            final ProductRequest request = new ProductRequest("떡볶이", null);

            // When & Then
            assertThatThrownBy(() -> productService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @DisplayName("저장된 모든 프로덕트를 반환한다")
    void list() {
        // Given
        assertThatCode(()->productService.list())
                .doesNotThrowAnyException();
    }
}
