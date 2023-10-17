package kitchenpos.api.product;

import kitchenpos.api.config.ApiTestConfig;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.ProductCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
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
        final String request = "{\n" +
                "  \"name\": \"강정치킨\",\n" +
                "  \"price\": 17000\n" +
                "}";

        // when
        // FIXME: domain -> dto 로 변경
        final Long expectedId = 1L;
        final Product expectedProduct = new Product();
        expectedProduct.setId(expectedId);
        when(productService.create(any(ProductCreateRequest.class))).thenReturn(expectedProduct);

        // then
        mockMvc.perform(post("/api/products")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl(String.format("/api/products/%d", expectedId)));
    }
}
