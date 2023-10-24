package kitchenpos.application;

import static kitchenpos.common.fixtures.ProductFixtures.PRODUCT1_NAME;
import static kitchenpos.common.fixtures.ProductFixtures.PRODUCT1_PRICE;
import static kitchenpos.common.fixtures.ProductFixtures.PRODUCT1_REQUEST;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import kitchenpos.common.ServiceTest;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.application.dto.ProductCreateRequest;
import kitchenpos.product.application.dto.ProductResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
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
            final ProductCreateRequest request = PRODUCT1_REQUEST();
            final Product product = new Product(PRODUCT1_NAME, PRODUCT1_PRICE);
            final Product savedProduct = productRepository.save(product);
            final ProductResponse expectedProductResponse = ProductResponse.from(savedProduct);

            // when
            final ProductResponse createdProduct = productService.create(request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(createdProduct.getId()).isNotNull();
                softly.assertThat(createdProduct).usingRecursiveComparison()
                        .ignoringFields("id").isEqualTo(expectedProductResponse);
            });
        }
    }
}
