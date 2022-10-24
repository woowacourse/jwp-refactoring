package kitchenpos.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;

@SpringBootTest
@Transactional
@TestConstructor(autowireMode = AutowireMode.ALL)
public class ProductServiceTest {

    private final ProductService productService;

    public ProductServiceTest(ProductService productService) {
        this.productService = productService;
    }

    @Test
    @DisplayName("상품의 가격이 존재하지 않는다면 예외가 발생한다.")
    public void createWithNotContainPrice() {
        Product product = new Product("삼겹살", null);

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품의 가격이 음수라면 예외가 발생한다.")
    public void createWithNegativePrice() {
        Product product = new Product("삼겹살", BigDecimal.valueOf(-1L));

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
