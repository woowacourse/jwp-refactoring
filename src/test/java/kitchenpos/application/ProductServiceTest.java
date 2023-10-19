package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.fixtures.Fixtures;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class ProductServiceTest extends ServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    Fixtures fixtures;

    @Nested
    class 상품_등록 {

        @Test
        void 상품을_등록한다() {
            // given
            Product product = new Product();
            product.setName("햄버거");
            product.setPrice(BigDecimal.valueOf(10_000));

            // when
            Product result = productService.create(product);

            // then
            assertThat(result.getName()).isEqualTo(product.getName());
            assertThat(product.getPrice().intValue()).isEqualTo(product.getPrice().intValue());
        }

        @Test
        void 가격이_null인_경우_예외가_발생한다() {
            // given
            Product product = new Product();
            product.setName("햄버거");
            product.setPrice(null);

            // when, then
            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, -100, -1231123})
        void 가격이_음수인_경우_예외가_발생한다(int value) {
            // given
            Product product = new Product();
            product.setName("햄버거");
            product.setPrice(BigDecimal.valueOf(value));

            // when, then
            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 모든_상품_목록을_불러온다() {
        // given
        Product product = fixtures.상품_저장("햄버거", 10_000L);

        // when
        List<Product> results = productService.list();

        // then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo(product.getName());
        assertThat(results.get(0).getPrice().intValue()).isEqualTo(product.getPrice().intValue());
    }

}
