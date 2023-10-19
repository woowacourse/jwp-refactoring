package kitchenpos.api.product;

import kitchenpos.api.config.ApiTestConfig;
import kitchenpos.domain.Product;
import kitchenpos.domain.vo.Price;
import kitchenpos.ui.dto.response.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductListApiTest extends ApiTestConfig {

    @DisplayName("상품 전체 조회 API 테스트")
    @Test
    void listProduct() throws Exception {
        // given
        final ProductResponse response = ProductResponse.from(new Product("pizza", new Price(BigDecimal.valueOf(17000))));

        // when
        when(productService.list()).thenReturn(List.of(response));

        // then
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(List.of(response))));
    }
}
