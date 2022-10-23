package kitchenpos.ui;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;

class ProductRestControllerTest extends ControllerTest {

    @DisplayName("상품을 등록한다.")
    @Test
    void create() throws Exception {
        // given
        ProductRequest productRequest = new ProductRequest("닭강정", BigDecimal.valueOf(5000L));
        ProductResponse productResponse = new ProductResponse(1L, "닭강정", BigDecimal.valueOf(5000L));

        given(productService.create(any(ProductRequest.class)))
            .willReturn(productResponse);

        // when
        ResultActions result = mockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productRequest)));

        // then
        result.andExpect(status().isCreated())
            .andExpect(header().string("location", "/api/products/1"))
            .andExpect(content().json(objectMapper.writeValueAsString(productResponse)));
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() throws Exception {
        // given
        List<ProductResponse> productResponses = List.of(
            new ProductResponse(1L, "닭강정", BigDecimal.valueOf(5000L)),
            new ProductResponse(2L, "후라이드 치킨", BigDecimal.valueOf(10000L))
        );

        given(productService.list())
            .willReturn(productResponses);

        // when
        ResultActions result = mockMvc.perform(get("/api/products"));

        // then
        result.andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(productResponses)));
    }
}
