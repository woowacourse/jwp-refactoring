package kitchenpos.product.application;

import kitchenpos.application.product.ProductService;
import kitchenpos.application.product.dto.ProductCreateRequest;
import kitchenpos.application.product.dto.ProductResponse;
import kitchenpos.common.ServiceTest;
import kitchenpos.common.fixtures.ProductFixtures;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Nested
    @DisplayName("Product 생성 시")
    class CreateProduct {

        @Test
        @DisplayName("생성에 성공한다.")
        void success() {
            // given
            final ProductCreateRequest request = ProductFixtures.PRODUCT1_REQUEST();
            final Product product = new Product(ProductFixtures.PRODUCT1_NAME, ProductFixtures.PRODUCT1_PRICE);
            final Product savedProduct = productRepository.save(product);
            final ProductResponse expectedProductResponse = ProductResponse.from(savedProduct);

            // when
            final ProductResponse createdProduct = productService.create(request);

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(createdProduct.getId()).isNotNull();
                softly.assertThat(createdProduct).usingRecursiveComparison()
                        .ignoringFields("id").isEqualTo(expectedProductResponse);
            });
        }
    }
}
