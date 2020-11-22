package kitchenpos.ui;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import kitchenpos.application.ProductService;
import kitchenpos.dto.ProductCreateRequest;
import kitchenpos.dto.ProductResponse;

@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest extends RestControllerTest {
    @MockBean
    private ProductService productService;

    @DisplayName("상품 생성 요청을 수행한다.")
    @Test
    void create() throws Exception {
        ProductCreateRequest productCreateRequest = new ProductCreateRequest("test", BigDecimal.TEN);
        ProductResponse productResponse = new ProductResponse(1L, productCreateRequest.getName(),
            productCreateRequest.getPrice());

        given(productService.create(any(ProductCreateRequest.class))).willReturn(productResponse);

        mockMvc.perform(post("/api/products")
            .content(objectMapper.writeValueAsString(productCreateRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/api/products/" + productResponse.getId()))
            .andDo(print());
    }

    @DisplayName("상품 전체 목록 조회 요청을 수행한다.")
    @Test
    void list() throws Exception {
        ProductResponse productResponse = new ProductResponse(1L, "test", BigDecimal.TEN);

        given(productService.list()).willReturn(Collections.singletonList(productResponse));

        mockMvc.perform(get("/api/products")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());
    }
}