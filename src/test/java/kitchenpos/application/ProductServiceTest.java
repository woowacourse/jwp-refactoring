package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Product;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.support.SpringBootNestedTest;

@SuppressWarnings("NonAsciiCharacters")
@Transactional
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 생성한다")
    @SpringBootNestedTest
    class CreateTest {

        @DisplayName("상품을 생성하면 ID가 할당된 Product객체가 반환된다")
        @Test
        void create() {
            Product product = ProductFixture.양념치킨.toProduct();

            Product actual = productService.create(product);
            assertThat(actual.getId()).isNotNull();
        }

        @DisplayName("가격이 null인 상품을 생성할시 예외가 발생한다")
        @Test
        void throwExceptionWhenWithNullPrice() {
            BigDecimal price = null;
            Product product = ProductFixture.양념치킨.toProduct();
            product.setPrice(price);

            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("가격이 음수인 상품을 생성할시 예외가 발생한다")
        @Test
        void throwExceptionWhenWithNegativePrice() {
            BigDecimal price = new BigDecimal(-1);
            Product product = ProductFixture.양념치킨.toProduct();
            product.setPrice(price);

            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("존재하는 모든 상품 목록을 조회한다")
    @Test
    void list() {
        int numOfProducts = 6;
        for (int i = 0; i < numOfProducts; i++) {
            productService.create(ProductFixture.양념치킨.toProduct());
        }

        List<Product> products = productService.list();

        assertThat(products).hasSize(numOfProducts);
    }
}
