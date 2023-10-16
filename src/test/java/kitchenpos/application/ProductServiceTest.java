package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.domain.Product;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;

    @Nested
    class 상품_생성_ {

        @Test
        void 정상_요청() {
            // given
            Product product = createProduct("피움 치킨", 18_000L);

            // when
            Product savedProduct = productService.create(product);

            // then
            SoftAssertions.assertSoftly(
                    softly -> {
                        softly.assertThat(savedProduct.getPrice()).isEqualByComparingTo(product.getPrice());
                        softly.assertThat(savedProduct.getName()).isEqualTo(product.getName());
                    }
            );
        }

        @Test
        void 가격없이_요청하면_예외_발생() {
            // given
            Product product = createProduct("조이 치킨", null);

            // when, then
            assertThatThrownBy(
                    () -> productService.create(product)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @ValueSource(longs = {-2L, -100L})
        void 가격이_0미만이면_예외_발생(long price) {
            // given
            Product product = createProduct("조이 치킨", price);

            // when, then
            assertThatThrownBy(
                    () -> productService.create(product)
            ).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 전체_상품_조회_ {

        @Test
        void 정상_요청() {
            // given
            Product product = createProduct("조이 치킨", 18_000L);
            Product savedProduct = productService.create(product);

            // when
            List<Product> products = productService.readAll();

            // then
            assertThat(products)
                    .extracting(Product::getId)
                    .contains(savedProduct.getId());
        }
    }

    private Product createProduct(final String name, final Long price) {
        if (price == null) {
            return new Product(name, null);
        }
        return new Product(name, BigDecimal.valueOf(price));
    }
}
