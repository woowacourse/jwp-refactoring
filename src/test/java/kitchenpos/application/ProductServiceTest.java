package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Product;

@Transactional
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 생성한다")
    @Nested
    class CreateTest {

        @DisplayName("상품을 생성하면 ID가 할당된 Product객체가 반환된다")
        @Test
        void create() {
            Product product = new Product("뿌링클", new BigDecimal(18_000));

            Product actual = productService.create(product);
            assertThat(actual.getId()).isNotNull();
        }

        @DisplayName("가격이 null인 상품을 생성할시 예외가 발생한다")
        @Test
        void throwExceptionWhenWithNullPrice() {
            BigDecimal price = null;
            Product product = new Product("뿌링클", price);

            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("가격이 음수인 상품을 생성할시 예외가 발생한다")
        @Test
        void throwExceptionWhenWithNegativePrice() {
            BigDecimal price = new BigDecimal(-1);
            Product product = new Product("뿌링클", price);

            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("존재하는 모든 상품 목록을 조회한다")
    @Test
    void list() {
        int numOfProducts = 6;
        Product product = new Product("뿌링클", new BigDecimal(18_000));
        for (int i = 0; i < numOfProducts; i++) {
            productService.create(product);
        }

        List<Product> products = productService.list();

        assertThat(products).hasSize(numOfProducts);
    }
}
