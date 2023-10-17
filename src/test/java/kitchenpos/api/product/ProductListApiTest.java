package kitchenpos.api.product;

import kitchenpos.api.config.ApiTestConfig;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductListApiTest extends ApiTestConfig {

    @DisplayName("상품 전체 조회 API 테스트")
    @Test
    void listProduct() throws Exception {
        // when
        // FIXME: domain -> dto 로 변경
        final Product expectedProduct = new Product(1L, "피자", BigDecimal.valueOf(17000));
        when(productService.list()).thenReturn(List.of(expectedProduct));

        // then
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].id", is(expectedProduct.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(expectedProduct.getName())))
                .andExpect(jsonPath("$[0].price", is(expectedProduct.getPrice().intValue())));
    }
}
