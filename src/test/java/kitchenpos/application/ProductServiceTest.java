package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Product;

@Transactional
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 정상적으로 생성한다")
    @Test
    void create() {
        Product product = new Product("짜장면", new BigDecimal(6000));

        Product actual = productService.create(product);
        assertThat(actual.getId()).isNotNull();
    }

    @DisplayName("가격이 null인 상품을 생성할시 예외가 발생한다")
    @Test
    void throwExceptionWhenCreateProductWithNullPrice() {
        BigDecimal price = null;
        Product product = new Product("짜장면", price);

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격이 음의 수인 상품을 생성할시 예외가 발생한다")
    @Test
    void throwExceptionWhenCreateProductWithNegativePrice() {
        BigDecimal price = new BigDecimal(-1000);
        Product product = new Product("짜장면", price);

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 정상적으로 조회한다")
    @Test
    void list() {
        List<Product> products = productService.list();

        assertThat(products).hasSize(6);
    }
}
