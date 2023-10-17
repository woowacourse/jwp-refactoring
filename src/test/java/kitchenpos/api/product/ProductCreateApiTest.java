package kitchenpos.api.product;

import kitchenpos.api.config.ApiTestConfig;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.request.ProductCreateRequest;
import kitchenpos.ui.dto.response.ProductCreateResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductCreateApiTest extends ApiTestConfig {

    @DisplayName("상품 생성 API 테스트")
    @Test
    void createProduct() throws Exception {
        // given
        final ProductCreateRequest request = new ProductCreateRequest("강정치킨", BigDecimal.valueOf(17000));

        // when
        final Product product = new Product(1L, request.getName(), request.getPrice());
        final ProductCreateResponse response = ProductCreateResponse.from(product);
        when(productService.create(eq(request))).thenReturn(response);

        // then
        mockMvc.perform(post("/api/products")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl(String.format("/api/products/%d", response.getId())));
    }
}
