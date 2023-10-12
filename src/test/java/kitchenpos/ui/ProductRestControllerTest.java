package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class ProductRestControllerTest extends ControllerTest {

    @Test
    void 상품_생성() throws Exception {
        // given
        Product product = 상품();
        String request = objectMapper.writeValueAsString(product);
        product.setId(1L);
        given(productService.create(any())).willReturn(product);
        String response = objectMapper.writeValueAsString(product);

        // when & then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(content().json(response));
    }

    @Test
    void 상품_조회() throws Exception {
        // given
        Product 상품1 = 상품();
        상품1.setId(1L);
        Product 상품2 = 상품();
        상품2.setId(2L);
        List<Product> products = List.of(상품1, 상품2);
        given(productService.list()).willReturn(products);
        String response = objectMapper.writeValueAsString(products);

        // when & then
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }
}
