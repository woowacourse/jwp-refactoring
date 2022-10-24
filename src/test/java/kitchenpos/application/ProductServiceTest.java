package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("create 메소드는 ")
    @Nested
    class CreateMethod {

        @DisplayName("Product를 생성한다.")
        @Test
        void Should_CreateProduct() {
            // given
            Product product = new Product("떡볶이", new BigDecimal(5000));

            // when
            Product actual = productService.create(product);

            // then
            assertAll(() -> {
                assertThat(actual.getName()).isEqualTo(product.getName());
                assertThat(actual.getPrice().doubleValue()).isEqualTo(product.getPrice().doubleValue());
            });
        }

        @DisplayName("Product의 price가 null인 경우 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_PriceOfProductIsNull() {
            // given
            Product product = new Product("떡볶이", null);

            // when & then
            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Proudct의 price가 0보다 작을 경우 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_PriceLessThan0() {
            // given
            Product product = new Product("떡볶이", new BigDecimal(-10000));

            // when & then
            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("list 메소드는")
    @Nested
    class ListMethod {

        @DisplayName("저장된 전체 상품 목록을 반환한다.")
        @Test
        void Should_ReturnAllProductList() {
            // given
            Product product1 = new Product("떡볶이", new BigDecimal(10000));
            Product product2 = new Product("라면", new BigDecimal(10000));
            Product product3 = new Product("김밥", new BigDecimal(10000));

            productService.create(product1);
            productService.create(product2);
            productService.create(product3);

            // when
            List<Product> actual = productService.list();

            // then
            assertAll(() -> {
                assertThat(actual).hasSize(3);
            });
        }
    }
}
