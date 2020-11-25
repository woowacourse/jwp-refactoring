package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.config.TruncateDatabaseConfig;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.product.ProductRequest;
import kitchenpos.ui.dto.product.ProductResponse;

class ProductServiceTest extends TruncateDatabaseConfig {

    @Autowired
    ProductService productService;

    @DisplayName("Product 생성")
    @Test
    void create() {
        ProductRequest request = new ProductRequest("상품1", BigDecimal.valueOf(1000L));
        ProductResponse savedProduct = productService.create(request);

        assertAll(
            ()-> assertThat(savedProduct.getName()).isEqualTo("상품1"),
            ()-> assertThat(savedProduct.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(1000L))
        );

    }

    @DisplayName("Product 생성 실패 - 가격 음수")
    @Test
    void create_When_NegativePrice() {
        ProductRequest request = new ProductRequest("상품1", BigDecimal.valueOf(-100L));
        assertThatThrownBy(()->productService.create(request))
        .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Product 생성 실패 - 가격 null")
    @ParameterizedTest
    @NullSource
    void create_When_Price_Null(BigDecimal price) {
        ProductRequest request = new ProductRequest("상품1", price);

        assertThatThrownBy(()->productService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Product 리스트 조회")
    @Test
    void list() {
        ProductRequest request1 = new ProductRequest("상품1", BigDecimal.valueOf(1000L));
        ProductRequest request2 = new ProductRequest("상품2", BigDecimal.valueOf(1000L));
        ProductRequest request3 = new ProductRequest("상품3", BigDecimal.valueOf(1000L));
        ProductResponse savedProduct1 = productService.create(request1);
        ProductResponse savedProduct2 = productService.create(request2);
        ProductResponse savedProduct3 = productService.create(request3);

        List<ProductResponse> productResponses = productService.list().getProductResponses();

        assertAll(
            () -> assertThat(productResponses).hasSize(3),
            () -> assertThat(productResponses.get(0).getName()).isEqualTo(savedProduct1.getName()),
            () -> assertThat(productResponses.get(1).getName()).isEqualTo(savedProduct2.getName()),
            () -> assertThat(productResponses.get(2).getName()).isEqualTo(savedProduct3.getName())
            );
    }
}
