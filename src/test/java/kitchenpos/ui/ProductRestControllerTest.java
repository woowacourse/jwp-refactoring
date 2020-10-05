package kitchenpos.ui;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.Product;

class ProductRestControllerTest extends ControllerTest {

    @DisplayName("create: 상품을 등록할 수 있다.")
    @Test
    void createTest() throws Exception {
        final Product product = new Product();
        product.setName("후라이드 치킨");
        product.setPrice(BigDecimal.valueOf(16000));
        create("/api/products", product)
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name").value("후라이드 치킨"));
    }

    @DisplayName("findProducts: 상품 목록을 조회할 수 있다.")
    @Test
    void findProductsTest() throws Exception {
        findList("/api/products")
                .andExpect(jsonPath("$[0].name").value("후라이드"))
                .andExpect(jsonPath("$[1].name").value("양념치킨"));
    }
}
