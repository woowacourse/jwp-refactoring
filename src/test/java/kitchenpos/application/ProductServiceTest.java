package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.common.ServiceTest;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

@ServiceTest
class ProductServiceTest {
    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 추가한다.")
    @Test
    void create() {
        Product product = new Product();
        product.setName("상품1");
        product.setPrice(BigDecimal.valueOf(1_000));

        Product actual = productService.create(product);

        assertAll(
            () -> assertThat(actual).extracting(Product::getId).isNotNull(),
            () -> assertThat(actual).extracting(Product::getName).isEqualTo(product.getName()),
            () -> assertThat(actual).extracting(Product::getPrice, BIG_DECIMAL).isEqualByComparingTo(product.getPrice())
        );
    }

    @DisplayName("상품을 추가할 시 가격이 null일 경우 예외 처리한다.")
    @Test
    void createWithNullPrice() {
        Product product = new Product();
        product.setName("상품1");
        product.setPrice(null);

        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품을 추가할 시 가격이 음수일 경우 예외 처리한다.")
    @Test
    void createWithNegativePrice() {
        Product product = new Product();
        product.setName("상품1");
        product.setPrice(BigDecimal.valueOf(-10L));

        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 전체 목록을 조회한다.")
    @Test
    void list() {
        Product product = new Product();
        product.setName("상품1");
        product.setPrice(BigDecimal.valueOf(1_000L));

        productDao.save(product);

        List<Product> actual = productService.list();

        assertThat(actual).hasSize(1);
    }
}