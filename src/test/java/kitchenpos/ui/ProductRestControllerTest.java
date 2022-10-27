package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.ui.dto.ProductRequest;
import kitchenpos.ui.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

public class ProductRestControllerTest extends ControllerTest {

    @DisplayName("POST /api/products")
    @Test
    void create() throws Exception {
        // given
        ProductRequest request = new ProductRequest("상품", 1000L);
        ProductResponse response = new ProductResponse(1L, "상품", 1000L);
        given(productApiService.create(any(ProductRequest.class))).willReturn(response);

        // when
        ResultActions result = mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        // then
        result.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/products/" + response.getId()));
    }

    @DisplayName("GET /api/products")
    @Test
    void list() throws Exception {
        // given
        List<ProductResponse> productResponses = List.of(
                new ProductResponse(1L, "상품1", 1000L),
                new ProductResponse(2L, "상품2", 2000L)
        );
        given(productApiService.list()).willReturn(productResponses);

        // when
        ResultActions result = mockMvc.perform(get("/api/products"));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(productResponses)));
    }
}
