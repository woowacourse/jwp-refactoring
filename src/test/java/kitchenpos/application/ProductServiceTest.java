package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.Product;

class ProductServiceTest extends ServiceTest {

    @Nested
    @DisplayName("create()")
    class CreateMethod {

        @Test
        @DisplayName("예외사항이 존재하지 않는 경우 새로운 상품을 생성한다.")
        void create() {
            // given
            Product product = createProduct("이름", new BigDecimal(1000));

            // when
            Product savedProduct = productService.create(product);

            // then
            assertThat(savedProduct.getId()).isNotNull();
        }

        @Test
        @DisplayName("가격이 빈값일 경우 예외가 발생한다.")
        void nullPrice() {
            // given
            Product product = createProduct("이름", null);

            // when, then
            assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("가격이 0 미만인 경우 예외가 발생한다.")
        void negativePrice() {
            // given
            Product product = createProduct("이름", BigDecimal.valueOf(-1));

            // when, then
            assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
        }

    }

    @Nested
    @DisplayName("list()")
    class ListMethod {

        @Test
        @DisplayName("전체 상품을 조회한다.")
        void list() {
            List<Product> products = productService.list();
            assertThat(products).isNotNull();
        }

    }

    private Product createProduct(String name, BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);

        return product;
    }

}
