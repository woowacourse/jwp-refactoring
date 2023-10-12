package kitchenpos.application.product;

import kitchenpos.application.ProductService;
import kitchenpos.application.config.ServiceTestConfig;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class ProductServiceTest extends ServiceTestConfig {

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productDao);
    }

    @DisplayName("상품 생성")
    @Nested
    class Create {
        @DisplayName("성공한다.")
        @Test
        void success() {
            // given
            final Product productInput = new Product();
            productInput.setName("여우곰탕");
            productInput.setPrice(BigDecimal.valueOf(10000));

            // when
            final Product actual = productService.create(productInput);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.getName()).isEqualTo(productInput.getName());
                softly.assertThat(actual.getPrice().compareTo(productInput.getPrice())).isZero();
            });
        }

        @DisplayName("가격이 null 이면 실패한다.")
        @Test
        void fail_if_price_is_null() {
            // given
            final Product productInput = new Product();
            productInput.setName("여우곰탕");
            productInput.setPrice(null);

            // then
            assertThatThrownBy(() -> productService.create(productInput))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("가격이 0원 미만이면 실패한다.")
        @Test
        void fail_if_price_under_zero() {
            // given
            final Product productInput = new Product();
            productInput.setName("여우곰탕");
            productInput.setPrice(BigDecimal.valueOf(-1));

            // then
            assertThatThrownBy(() -> productService.create(productInput))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
