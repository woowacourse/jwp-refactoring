package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import kitchenpos.dto.product.ProductRequest;
import kitchenpos.dto.product.ProductResponse;

@Import(ProductService.class)
class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("create: 상품 생성")
    @Test
    void create() {
        final ProductRequest productRequest = new ProductRequest("매콤치킨", BigDecimal.valueOf(16000));
        final ProductResponse actual = productService.create(productRequest);

        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo("매콤치킨"),
                () -> assertThat(actual.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(16000))
        );
    }

    @DisplayName("list: 상품 전체 조회")
    @Test
    void list() {
        final ProductRequest productRequest1 = new ProductRequest("매콤치킨", BigDecimal.valueOf(16000));
        final ProductRequest productRequest2 = new ProductRequest("간장치킨", BigDecimal.valueOf(16000));
        productService.create(productRequest1);
        productService.create(productRequest2);

        final List<ProductResponse> actual = productService.list();

        assertThat(actual).hasSize(2);
    }
}