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
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.domain.menu.Product;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Nested
    @DisplayName("Product 생성 테스트")
    class CreateProductTest {

        @Test
        @DisplayName("프로덕트를 생성한다.")
        void createProduct() {
            // Given
            Product product = new Product();
            product.setPrice(BigDecimal.TEN);
            product.setName("치킨");

            // When
            Product createdProduct = productService.create(product);


            // Then
            assertSoftly(softAssertions -> {
                assertThat(createdProduct.getId()).isNotNull();
                assertThat(createdProduct.getName()).isEqualTo(product.getName());
                assertThat(createdProduct.getPrice()).isEqualByComparingTo(product.getPrice());
            });
        }

        @Test
        @DisplayName("가격이 0미만인 경우 예외가 발생한다.")
        void createProductWithSubZeroPrice() {
            // Given
            Product product = new Product();
            product.setPrice(BigDecimal.valueOf(-10));

            // When & Then
            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("가격이 null인 경우 예외가 발생한다.")
        void createProductWithNullPrice() {
            // Given
            Product product = new Product();

            // When & Then
            assertThatThrownBy(() -> productService.create(product))
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
