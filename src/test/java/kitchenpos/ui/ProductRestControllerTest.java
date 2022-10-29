package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dto.request.ProductCreateRequest;
import kitchenpos.dto.response.ProductResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

class ProductRestControllerTest extends RestControllerTest {

    @Test
    void 상품_생성에_성공한다() throws Exception {
        ProductCreateRequest request = new ProductCreateRequest("상품", BigDecimal.valueOf(1_000));
        ProductResponse expected = new ProductResponse(1L, "상품", BigDecimal.valueOf(1_000));

        when(productService.create(any(ProductCreateRequest.class))).thenReturn(expected);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andDo(print());
    }

    @Test
    void 상품_목록_조회에_성공한다() throws Exception {
        ProductResponse expected = new ProductResponse(1L, "상품", BigDecimal.valueOf(1_000));

        when(productService.list()).thenReturn(List.of(expected));

        MvcResult mvcResult = mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        List<ProductResponse> content = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(),
                new TypeReference<List<ProductResponse>>() {
                });

        assertThat(content).hasSize(1)
                .extracting("id")
                .containsExactly(1L);
    }
}
