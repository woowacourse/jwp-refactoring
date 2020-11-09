package kitchenpos.application;

import static kitchenpos.fixture.RequestFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import kitchenpos.application.response.ProductResponse;

@Import(ProductService.class)
class ProductServiceTest extends ApplicationServiceTest {
    @Autowired
    private ProductService productService;

    @DisplayName("상품 생성")
    @Test
    void create() {
        Long productId = productService.create(PRODUCT_REQUEST).getId();

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