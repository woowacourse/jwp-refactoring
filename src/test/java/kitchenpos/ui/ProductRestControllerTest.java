package kitchenpos.ui;

import kitchenpos.application.ProductService;
import kitchenpos.ui.dto.product.ProductRequest;
import kitchenpos.ui.dto.product.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;

import static kitchenpos.ProductFixture.PRODUCT_NAME1;
import static kitchenpos.ProductFixture.PRODUCT_PRICE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest extends ControllerTest {

    @MockBean
    private ProductService productService;

    @DisplayName("상품을 생성할 수 있다.")
    @Test
    void create() throws Exception {
        final Long productId = 1L;
        final ProductRequest request = new ProductRequest(PRODUCT_NAME1, PRODUCT_PRICE);
        final ProductResponse response = new ProductResponse(1L, PRODUCT_NAME1, PRODUCT_PRICE);
        when(productService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/products")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/products/" + productId))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void list() throws Exception {
        final List<ProductResponse> response = Collections.singletonList(new ProductResponse(1L, PRODUCT_NAME1, PRODUCT_PRICE));

        when(productService.list()).thenReturn(response);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
}
