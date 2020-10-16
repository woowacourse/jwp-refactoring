package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("create: 상품 등록 확인 테스트")
    @Test
    void createTest() {
        final ProductRequest productRequest = new ProductRequest("후라이드 치킨", BigDecimal.valueOf(16000));

        final ProductResponse productResponse = productService.create(productRequest);

        assertAll(
                () -> assertThat(productResponse.getId()).isNotNull(),
                () -> assertThat(productResponse.getName()).isEqualTo(productRequest.getName()),
                () -> assertThat(productResponse.getPrice()).isEqualByComparingTo(productRequest.getPrice())
        );
    }

    @DisplayName("findProducts: 상품 전체 목록 조회 확인 테스트")
    @Test
    void findProductsTest() {
        final ProductRequest productRequest = new ProductRequest("후라이드 치킨", BigDecimal.valueOf(16000));

        productService.create(productRequest);

        final List<ProductResponse> products = productService.list();

        assertAll(
                () -> assertThat(products).hasSize(1)
        );
    }
}
