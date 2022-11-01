package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.ControllerTest;
import kitchenpos.menu.application.ProductService;
import kitchenpos.menu.application.dto.ProductCreateRequest;
import kitchenpos.menu.application.dto.ProductResponse;
import kitchenpos.menu.ui.ProductRestController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest extends ControllerTest {

    private static final String PRODUCT_URL = "/api/products";

    private final ProductResponse productResponse = new ProductResponse(1L, "pasta", BigDecimal.valueOf(13000));

    @Autowired
    private ProductService productService;

    @Test
    void 상품을_생성할_수_있다() throws Exception {
        // given
        when(productService.create(any(ProductCreateRequest.class))).thenReturn(productResponse);

        // when
        ResultActions response = postRequestWithJson(PRODUCT_URL, new ProductCreateRequest());

        // then
        response.andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(productResponse)));
    }

    @Test
    void 상품_목록을_조회할_수_있다() throws Exception {
        // given
        List<ProductResponse> productResponses = Arrays.asList(productResponse);
        when(productService.list()).thenReturn(productResponses);

        // when
        ResultActions response = getRequest(PRODUCT_URL);

        // then
        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(productResponses)));
    }
}
