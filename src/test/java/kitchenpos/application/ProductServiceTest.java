package kitchenpos.application;

import static kitchenpos.domain.DomainCreator.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

@SpringBootTest
@Transactional
@Sql("classpath:delete.sql")
class ProductServiceTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productDao);
    }

    @Test
    @DisplayName("상품을 생성할 수 있어야 한다.")
    void create() {
        Product product = createProduct("product", BigDecimal.valueOf(1000));

        Product createdProduct = productService.create(product);

        assertAll(
            () -> assertThat(createdProduct.getId()).isNotNull(),
            () -> assertThat(createdProduct.getName()).isEqualTo(product.getName()),
            () -> assertThat(createdProduct.getPrice().toBigInteger()).isEqualTo(product.getPrice().toBigInteger())
        );
    }

    @Test
    @DisplayName("상품의 가격은 양수여야 한다.")
    void createFail() {
        Product product = createProduct("product", BigDecimal.valueOf(-1));
        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 목록을 불러올 수 있어야 한다.")
    void list() {
        Product product1 = createProduct("product1", BigDecimal.valueOf(1000));
        Product product2 = createProduct("product2", BigDecimal.valueOf(1000));
        productService.create(product1);
        productService.create(product2);

        List<Product> savedProducts = productService.list();

        assertThat(savedProducts.size()).isEqualTo(2);
    }
}
