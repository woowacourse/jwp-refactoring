package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ProductService의")
class ProductServiceTest extends ServiceTest {

    @Nested
    @DisplayName("create 메서드는")
    class Create {

        @Test
        @DisplayName("상품을 생성한다.")
        void create_validProduct_success() {
            // given
            final Product expected = new Product();
            expected.setName("치킨");
            expected.setPrice(BigDecimal.valueOf(1000));

            // when
            final Product actual = productService.create(expected);

            // then
            softly.assertThat(actual.getName()).isEqualTo(expected.getName());
            softly.assertThat(actual.getPrice()).isEqualByComparingTo(expected.getPrice());
            softly.assertThat(actual.getId()).isNotNull()
                    .isPositive();
            softly.assertAll();
        }

        @Test
        @DisplayName("상품의 가격이 null일 수 없다.")
        void create_priceIsNull_exception() {
            // given
            final Product product = new Product();
            product.setName("치킨");
            product.setPrice(null);

            // when & then
            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("상품의 가격이 음수일 수 없다.")
        void create_priceIsNegative_exception() {
            // given
            final Product product = new Product();
            product.setName("치킨");
            product.setPrice(BigDecimal.valueOf(-1));

            // when & then
            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("list 메서드는")
    class ListTest {

        @Test
        @DisplayName("상품 목록을 조회한다.")
        void list_savedProducts_success() {
            // given
            final String name1 = "치킨";
            final String name2 = "피자";
            final String name3 = "족발";
            final String name4 = "보쌈";

            saveProduct(name1);
            saveProduct(name2);
            saveProduct(name3);
            saveProduct(name4);

            // when
            final List<Product> actual = productService.list();

            // then
            assertThat(actual).extracting(Product::getName)
                    .containsExactly(name1, name2, name3, name4);
        }
    }
}
