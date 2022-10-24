package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("ProductService의")
class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;

    @Nested
    @DisplayName("create 메서드는")
    class Create {

        @Test
        @DisplayName("상품의 이름과 가격을 받으면, 상품을 저장하고 내용을 반환한다.")
        void success() {
            //given
            Product product = new Product();
            product.setName("productA");
            product.setPrice(BigDecimal.valueOf(1000));

            //when
            Product actual = productService.create(product);

            //then
            assertThat(actual.getName()).isEqualTo("productA");
        }

        @Test
        @DisplayName("상품의 가격이 없으면, 예외를 던진다.")
        void fail_noPrice() {
            //given
            Product product = new Product();
            product.setName("productA");
            product.setPrice(null);

            //when & then
            Assertions.assertThatThrownBy(
                    () -> productService.create(product)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("상품의 가격이 0보다 작으면, 예외를 던진다.")
        void fail_zeroOrNegativePrice() {
            //given
            Product product = new Product();
            product.setName("productA");
            product.setPrice(BigDecimal.valueOf(-1));

            //when & then
            Assertions.assertThatThrownBy(
                    () -> productService.create(product)
            ).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
