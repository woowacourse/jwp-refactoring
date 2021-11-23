package kitchenpos.application;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;

import static org.assertj.core.api.Assertions.assertThat;

class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("상품 등록")
    void createTest() {

        // given
        final ProductRequest productRequest = Fixtures.makeProduct();

        // when
        final Product savedProduct = productService.create(productRequest);

        // then
        assertThat(productService.list()).contains(savedProduct);
    }
}
