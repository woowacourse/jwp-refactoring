package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.product.application.dto.ProductCreationRequest;
import kitchenpos.product.application.dto.ProductResult;
import kitchenpos.support.IntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProductServiceTest extends IntegrationTest {

    @Autowired
    private ProductService productService;

    @Test
    void create_product_success() {
        // given
        final ProductCreationRequest request = new ProductCreationRequest("chicken", BigDecimal.valueOf(10000L));

        // when
        final ProductResult savedProduct = productService.create(request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(savedProduct.getId()).isNotNull();
            softly.assertThat(savedProduct.getName()).isEqualTo("chicken");
        });
    }

    @Nested
    class create_product_failure {

        @Test
        void product_price_is_under_zero() {
            // given
            final BigDecimal priceUnderZero = BigDecimal.valueOf(-1000L);
            final ProductCreationRequest request = new ProductCreationRequest("chicken", priceUnderZero);

            // when & then
            assertThatThrownBy(() -> productService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Price must be greater than zero.");
        }

        @Test
        void product_price_is_null() {
            // given
            final ProductCreationRequest request = new ProductCreationRequest("chicken", null);

            // when & then
            assertThatThrownBy(() -> productService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Price must not be null.");
        }
    }

    @Test
    void list() {
        // given
        generateProduct("chicken", 10000L);
        generateProduct("chicken-2", 10000L);

        // when
        final List<ProductResult> products = productService.list();

        // then
        assertThat(products).hasSize(2);
    }
}
