package kitchenpos.application;

import static kitchenpos.fixture.RequestFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.application.dto.ProductResponse;

@SpringBootTest
class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @DisplayName("상품 생성")
    @Test
    void create() {
        Long productId = productService.create(PRODUCT_REQUEST);

        assertThat(productId).isNotNull();
    }

    @DisplayName("상품 전체 조회")
    @Test
    void list() {
        productService.create(PRODUCT_REQUEST);
        List<ProductResponse> list = productService.list();

        assertThat(list.isEmpty()).isFalse();
    }
}