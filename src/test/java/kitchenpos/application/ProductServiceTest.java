package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql(value = "classpath:data/truncate.sql")
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    void create() {
        // given
        final Product product = new Product("강정치킨", BigDecimal.valueOf(17000));

        // when
        final Product result = productService.create(product);

        // then
        assertThat(result.getId()).isNotNull();
    }

    @Test
    void create_priceException() {
        // given
        final Product product = new Product("강정치킨", BigDecimal.valueOf(-1));

        // when & then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        // given
        productService.create(new Product("Product1", BigDecimal.valueOf(1000)));
        productService.create(new Product("Product2", BigDecimal.valueOf(2000)));

        // when
        final List<Product> result = productService.list();

        // then
        assertThat(result).hasSize(2);
    }
}
