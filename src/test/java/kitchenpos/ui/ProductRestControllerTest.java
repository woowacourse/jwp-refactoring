package kitchenpos.ui;

import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

import static kitchenpos.fixture.FixtureFactory.createProduct;
import static kitchenpos.ui.ProductRestController.PRODUCT_API;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = ProductRestController.class)
class ProductRestControllerTest extends ControllerTest {
    @MockBean
    private ProductService productService;

    @DisplayName("상품 생성 요청")
    @Test
    void create() throws Exception {
        Product request = createProduct(null, "강정치킨", BigDecimal.valueOf(17_000));
        String body = objectMapper.writeValueAsString(request);

        when(productService.create(any())).thenReturn(new Product());

        requestWithPost(PRODUCT_API, body);
    }

    @DisplayName("상품 목록 조회 요청")
    @Test
    void list() throws Exception {
        requestWithGet(PRODUCT_API);
    }
}