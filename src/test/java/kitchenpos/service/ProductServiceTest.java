package kitchenpos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.ProductService;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

@SpringBootTest
@Transactional
@TestConstructor(autowireMode = AutowireMode.ALL)
public class ProductServiceTest {

    private final ProductService productService;
    private final ProductDao productDao;

    public ProductServiceTest(ProductService productService, ProductDao productDao) {
        this.productService = productService;
        this.productDao = productDao;
    }

    @DisplayName("상품의 가격이 존재하지 않는다면 예외가 발생한다.")
    @Test
    public void createWithNotContainPrice() {
        Product product = new Product("삼겹살", null);

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 가격이 음수라면 예외가 발생한다.")
    @Test
    public void createWithNegativePrice() {
        Product product = new Product("삼겹살", BigDecimal.valueOf(-1L));

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 목록을 가져올 수 있다.")
    @Test
    public void list() {
        Product product1 = new Product("삼겹살", BigDecimal.valueOf(1000L));
        Product product2 = new Product("대패삼겹살", BigDecimal.valueOf(1000L));
        productDao.save(product1);
        productDao.save(product2);

        assertThat(productService.list()).hasSize(2);
    }
}
