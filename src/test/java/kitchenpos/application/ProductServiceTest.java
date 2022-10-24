package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    void create() {
        Product product = productService.create(new Product("제육", BigDecimal.ONE));

        assertThat(product.getId()).isNotNull();
    }

    @Test
    void list() {
        productService.create(new Product("제육", BigDecimal.ONE));

        assertThat(productService.list()).hasSize(1);
    }
}
