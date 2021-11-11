package kitchenpos.application;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductServiceTest extends IntegrationTest {

    @Test
    void create() {
        // given
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(1000));
        product.setName("product");

        // when
        Product savedProduct = productService.create(product);

        // then
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo("product");
        assertThat(savedProduct.getPrice().longValue()).isEqualTo(1000L);
    }

    @Test
    void list() {
        List<Product> products = productService.list();

        assertThat(products).hasSize(6);
    }
}