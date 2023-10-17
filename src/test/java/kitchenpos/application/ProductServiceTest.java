package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProductServiceTest extends ServiceIntegrationTest {
    private static final BigDecimal INVALID_PRICE = BigDecimal.valueOf(-1);
    
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
        final Product product = new Product("강정치킨", INVALID_PRICE);

        // when & then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        // given
        final Product product1 = productService.create(new Product("Product1", BigDecimal.valueOf(1000)));
        final Product product2 = productService.create(new Product("Product2", BigDecimal.valueOf(2000)));

        // when
        final List<Product> result = productService.list();

        // then
        assertThat(result).hasSize(2)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(product1, product2);
    }
}
