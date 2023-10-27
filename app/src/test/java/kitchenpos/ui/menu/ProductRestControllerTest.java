package kitchenpos.ui.menu;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.menu.dto.CreateProductResponse;
import kitchenpos.application.menu.dto.SearchProductResponse;
import kitchenpos.domain.menu.Price;
import kitchenpos.domain.menu.Product;
import kitchenpos.ui.ControllerTest;
import kitchenpos.ui.menu.dto.CreateProductRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class ProductRestControllerTest extends ControllerTest {

    @Test
    void 상품_생성() throws Exception {
        // given
        CreateProductRequest createProductRequest = new CreateProductRequest("강정치킨", BigDecimal.valueOf(17000));
        String request = objectMapper.writeValueAsString(createProductRequest);

        Product product = new Product(1L, "강정치킨", new Price(BigDecimal.valueOf(17000)));
        CreateProductResponse createProductResponse = CreateProductResponse.from(product);

        given(productService.create(any())).willReturn(createProductResponse);
        String response = objectMapper.writeValueAsString(createProductResponse);

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
        List<SearchProductResponse> productResponses = List.of(
                SearchProductResponse.from(new Product(1L, "강정치킨", new Price(BigDecimal.valueOf(17000)))),
                SearchProductResponse.from(new Product(2L, "강정치킨", new Price(BigDecimal.valueOf(17000))))
        );

        given(productService.list()).willReturn(productResponses);
        String response = objectMapper.writeValueAsString(productResponses);

        // when & then
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }
}
