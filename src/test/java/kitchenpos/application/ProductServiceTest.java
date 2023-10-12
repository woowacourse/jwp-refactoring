package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:truncate.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    void 상품을_등록할_수_있다() {
        // given
        final Product product = new Product();
        product.setName("테스트 상품");
        product.setPrice(new BigDecimal(2000));

        // when
        final Product result = productService.create(product);

        // then
        assertThat(result.getName()).isEqualTo(product.getName());
        assertThat(result.getPrice()).isEqualByComparingTo(product.getPrice());
    }

    @NullSource
    @ValueSource(strings = {"-1"})
    @ParameterizedTest
    void 상품가격이_없거나_0보다_작을경우_예외가_발생한다(final BigDecimal price) {
        // given
        final Product product = new Product();
        product.setName("테스트 상품");
        product.setPrice(price);

        // when then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품조회를_할_수_있다() {
        // given
        final Product product = new Product();
        product.setName("테스트 상품");
        product.setPrice(new BigDecimal(2000));
        productService.create(product);

        // when
        final List<Product> results = productService.list();

        // then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo(product.getName());
        assertThat(results.get(0).getPrice()).isEqualByComparingTo(product.getPrice());
    }
}
