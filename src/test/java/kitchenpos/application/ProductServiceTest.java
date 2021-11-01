package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품 생성 테스트")
    @Nested
    class ProductCreate {

        @DisplayName("[성공] 새로운 상품 등록")
        @Test
        void create_Success() {
            // given
            Product product = newProduct();

            // when
            Product savedProduct = productService.create(product);

            // then
            assertThat(savedProduct.getId()).isNotNull();
            assertThat(savedProduct)
                .extracting(in -> tuple(in.getName(), in.getPrice().toBigInteger()))
                .isEqualTo(tuple(product.getName(), product.getPrice().toBigInteger()));
        }

        @DisplayName("[실패] 가격이 null 이면 예외 발생")
        @Test
        void create_nullPrice_ExceptionThrown() {
            // given
            Product product = newProduct();
            product.setPrice(null);

            // when
            // then
            assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("[실패] 가격이 음수면 예외 발생")
        @Test
        void create_negativePrice_ExceptionThrown() {
            // given
            Product product = newProduct();
            product.setPrice(BigDecimal.valueOf(-10));

            // when
            // then
            assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("[성공] 전체 상품 조회")
    @Test
    void list_Success() {
        // given
        int previousSize = productService.list().size();
        productService.create(newProduct());

        // when
        List<Product> result = productService.list();

        // then
        assertThat(result).hasSize(previousSize + 1);
    }

    private Product newProduct() {
        Product product = new Product();
        product.setName("새로운 상품");
        product.setPrice(BigDecimal.valueOf(15_000));

        return product;
    }
}
