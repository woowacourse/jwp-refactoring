package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProductServiceTest extends IntegrationTest {

    @Autowired
    private ProductService productService;

    @Test
    void create_product_success() {
        // given
        final Product product = new Product();
        final BigDecimal price = BigDecimal.valueOf(10000);
        product.setName("chicken");
        product.setPrice(price);

        // when
        final Product savedProduct = productService.create(product);

        // then
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo("chicken");
    }

    @Nested
    class create_product_failure {

        @Test
        void product_price_is_under_zero() {
            // given
            final Product product = new Product();
            product.setName("chicken");
            product.setPrice(BigDecimal.valueOf(-1000));

            // when & then
            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void product_price_is_null() {
            // given
            final Product product = new Product();
            product.setName("chicken");
            product.setPrice(null);

            // when & then
            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
