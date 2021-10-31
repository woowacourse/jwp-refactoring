package kitchenpos.application;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.Product;

import static org.assertj.core.api.Assertions.assertThat;

class ProductServiceTest extends ServiceTest {

    private final Product product = new Product(null, "당수육", BigDecimal.valueOf(10000));

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("상품 등록")
    void createTest() {

        // when
        final Product savedProduct = productService.create(product);

        // then
        assertThat(productService.list()).contains(savedProduct);
    }
}
