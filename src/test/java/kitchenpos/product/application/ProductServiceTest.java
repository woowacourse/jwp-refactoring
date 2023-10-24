package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.product.domain.Product;
import kitchenpos.test.fixtures.ProductFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest {
    @Autowired
    ProductService productService;

    @Nested
    @DisplayName("상품을 등록할 때, ")
    class CreateProduct {
        @Test
        @DisplayName("정상 등록된다")
        void createProduct() {
            // given
            final Product product = ProductFixtures.BASIC.get();

            // when
            final Product saved = productService.create(product);

            // then
            assertSoftly(softly -> {
                softly.assertThat(saved.name()).isEqualTo(product.name());
                softly.assertThat(saved.price().intValue()).isEqualTo(product.price().intValue());
            });
        }

        @Test
        @DisplayName("상품 가격이 null일 시 예외 발생")
        void productPriceNullException() {
            // given
            final Product product = ProductFixtures.BASIC.get();
            product.setPrice(null);

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> productService.create(product));
        }

        @Test
        @DisplayName("상품 가격이 0원 미만일 시 예외 발생")
        void productPriceLessThanZeroWonException() {
            // given
            final Product product = ProductFixtures.BASIC.get();
            product.setPrice(new BigDecimal("-1"));

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> productService.create(product));
        }
    }

    @Test
    @DisplayName("상품 목록을 조회한다")
    void getProducts() {
        // given
        productService.create(ProductFixtures.BASIC.get());

        // when
        final List<Product> actualProducts = productService.list();

        // then
        assertThat(actualProducts).isNotEmpty();
    }
}
