package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    void 상품을_생성할_수_있다() {
        Product product = new Product("제품1", new BigDecimal(10000));

        Product actual = productService.create(product);

        assertAll(() -> {
            assertThat(actual.getId()).isNotNull();
            assertThat(actual.getName()).isEqualTo("제품1");
            assertThat(actual.getPrice().compareTo(new BigDecimal(10000))).isEqualTo(0);
        });
    }

    @Test
    void 상품의_가격이_음수인_경우_상품을_생성할_수_없다() {
        Product product = new Product("제품1", new BigDecimal(-1));

        assertThatThrownBy(() -> productService.create(product)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 전체_상품_목록을_조회할_수_있다() {
        Product product1 = new Product("제품1", new BigDecimal(10000));
        Product product2 = new Product("제품2", new BigDecimal(20000));

        productService.create(product1);
        productService.create(product2);

        List<Product> actual = productService.list();

        assertThat(actual).hasSize(2);
    }
}
