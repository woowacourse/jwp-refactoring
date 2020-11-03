package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.config.TruncateDatabaseConfig;
import kitchenpos.domain.Product;

class ProductServiceTest extends TruncateDatabaseConfig {

    @Autowired
    ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setName("상품1");
        product.setPrice(BigDecimal.valueOf(1000L));
    }

    @DisplayName("Product 생성")
    @Test
    void create() {
        Product savedProduct = productService.create(product);

        assertAll(
            ()-> assertThat(savedProduct.getName()).isEqualTo("상품1"),
            ()-> assertThat(savedProduct.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(1000L))
        );

    }

    @DisplayName("Product 생성 실패 - 가격 음수")
    @Test
    void create_When_NegativePrice() {
        product.setPrice(BigDecimal.valueOf(-100L));

        assertThatThrownBy(()->productService.create(product))
        .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Product 생성 실패 - 가격 null")
    @ParameterizedTest
    @NullSource
    void create_When_Price_Null(BigDecimal price) {
        product.setPrice(price);

        assertThatThrownBy(()->productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Product 리스트 조회")
    @Test
    void list() {
        Product savedProduct = productService.create(product);

        List<Product> productList = productService.list();

        assertAll(
            () -> assertThat(productList.size()).isEqualTo(1),
            () -> assertThat(productList.get(0).getName()).isEqualTo(savedProduct.getName()),
            () -> assertThat(productList.get(0).getPrice()).isEqualTo(savedProduct.getPrice())
            );
    }
}
