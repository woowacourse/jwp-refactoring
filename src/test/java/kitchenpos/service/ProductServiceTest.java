package kitchenpos.service;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.data.Percentage.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;

public class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("메뉴를 등록한다.")
    void create() {
        // given
        Product product = new Product("후라이드", BigDecimal.valueOf(16000));

        // when
        Product savedProduct = productService.create(product);

        // then
        assertThat(savedProduct.getPrice()).isCloseTo(product.getPrice(), withPercentage(0.0001));
        assertThat(savedProduct).usingRecursiveComparison()
            .ignoringFields("id", "price")
            .isEqualTo(product);
    }

    @Test
    @DisplayName("가격이 음수면 예외를 반환한다.")
    void create_price_negative() {
        // given
        Product product = new Product("후라이드", BigDecimal.valueOf(-1));

        // when, then
        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("전체 메뉴를 조회한다.")
    void list() {
        // given
        Product product = new Product("후라이드", BigDecimal.valueOf(16000));
        Product savedProduct = productService.create(product);

        // when
        List<Product> result = productService.list();

        // then
        assertThat(result).contains(savedProduct);
    }
}
