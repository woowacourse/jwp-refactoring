package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

public class ProductServiceTest extends ServiceTest {

    @Autowired
    protected ProductService productService;
    @Autowired
    protected ProductDao productDao;

    @Test
    @DisplayName("상품을 저장한다")
    void create() {
        // given
        Product product = new Product();
        product.setName("test");
        product.setPrice(BigDecimal.ONE);

        // when
        Product createdProduct = productService.create(product);

        // then
        assertAll(
            () -> assertThat(createdProduct).isNotNull(),
            () -> assertThat(createdProduct.getName()).isEqualTo("test")
        );
    }

    @Test
    @DisplayName("상품 가격은 함께 등록되어야 한다")
    void nullPrice() {
        // given
        Product product = new Product();
        product.setName("test");

        // when, then
        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 가격은 음수일 수 없다")
    void minusPrice() {
        // given
        Product product = new Product();
        product.setName("test");
        product.setPrice(BigDecimal.valueOf(-100));

        // when, then
        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 목록을 조회한다")
    void list() {
        // given
        int default_data_size = 6;

        // when
        List<Product> products = productService.list();

        // then
        assertAll(
            () -> assertThat(products).hasSize(default_data_size)
        );
    }
}
