package kitchenpos.application;

import static kitchenpos.test.fixture.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.test.ServiceTest;
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
    class 상품_추가_시 {

        @Test
        void 정상적인_상품이라면_상품을_추가한다() {
            Product product = 상품("텐동", BigDecimal.valueOf(11000));

            Product savedProduct = productService.create(product);

            assertSoftly(softly -> {
                softly.assertThat(savedProduct.getId()).isNotNull();
                softly.assertThat(savedProduct.getName()).isEqualTo("텐동");
                softly.assertThat(savedProduct.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(11000));
            });
        }

        @Test
        void 가격이_NULL이라면_예외를_던진다() {
            Product product = 상품("텐동", null);

            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @ValueSource(longs = {Integer.MIN_VALUE, -1})
        void 가격이_0보다_작으면_예외를_던진다(long price) {
            Product product = 상품("텐동", BigDecimal.valueOf(price));

            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 상품_목록_조회_시 {

        @Test
        void 모든_상품_목록을_조회한다() {
            Product productA = 상품("텐동", BigDecimal.valueOf(11000));
            Product productB = 상품("사케동", BigDecimal.valueOf(12000));
            Product savedProductA = productService.create(productA);
            Product savedProductB = productService.create(productB);

            List<Product> products = productService.list();

            assertThat(products).usingRecursiveComparison().isEqualTo(List.of(savedProductA, savedProductB));
        }

        @Test
        void 상품이_존재하지_않으면_목록이_비어있다() {
            List<Product> products = productService.list();

            assertThat(products).isEmpty();
        }
    }
}
