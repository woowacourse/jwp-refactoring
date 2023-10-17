package kitchenpos.application;

import kitchenpos.ServiceTest;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품 생성")
    @Nested
    class ProductCreateTest {

        @DisplayName("상품을 생성한다.")
        @Test
        void productCreate() {
            //given
            final Product product = new Product("product", new BigDecimal(1000));

            //when
            final Product actual = productService.create(product);

            //then
            assertSoftly(softly -> {
                softly.assertThat(actual.getId()).isNotNull();
                softly.assertThat(actual.getPrice()).isEqualByComparingTo(product.getPrice());
                softly.assertThat(actual.getName()).isEqualTo(product.getName());
            });
        }

        @DisplayName("가격이 null이면 상품 생성을 실패한다.")
        @Test
        void productCreateFailWhenPriceIsNull() {
            //given
            final Product product = new Product("product", null);

            // when & then
            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("가격이 음수면 상품 생성을 실패한다.")
        @Test
        void productCreateFailWhenPriceLessThenZero() {
            //given
            final Product product = new Product("product", new BigDecimal(-1));

            // when & then
            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("상품 조회 테스트")
    @Nested
    class ProductFindTest {

        @DisplayName("상품을 전체 조회한다.")
        @Test
        void productFindAll() {
            //given
            final Product product = new Product("product", new BigDecimal(1000));
            final Product expected = testFixtureBuilder.buildProduct(product);

            //when
            final List<Product> actual = productService.list();

            //then
            assertSoftly(softly -> {
                softly.assertThat(actual.size()).isEqualTo(1);
                softly.assertThat(actual.get(0).getId()).isEqualTo(expected.getId());
                softly.assertThat(actual.get(0).getPrice()).isEqualByComparingTo(expected.getPrice());
                softly.assertThat(actual.get(0).getName()).isEqualTo(expected.getName());
            });
        }
    }
}
