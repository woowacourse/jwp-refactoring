package kitchenpos.menu.fixture;

import kitchenpos.menu.application.ProductService;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;
import kitchenpos.menu.ui.ProductRestController;
import kitchenpos.support.RestControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;

import static kitchenpos.menu.fixture.ProductFixture.createProductRequest;
import static kitchenpos.menu.fixture.ProductFixture.createProductResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest extends RestControllerTest {

    @MockBean
    private ProductService mockProductService;

    @DisplayName("제품 생성 요청을 처리한다.")
    @Test
    void create() throws Exception {
        ProductRequest productRequest = createProductRequest();
        ProductResponse productResponse = createProductResponse(1L, productRequest);
        when(mockProductService.create(any())).thenReturn(productResponse);
        mockMvc.perform(post("/api/products")
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest))
                )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/products/" + productResponse.getId()))
                .andExpect(content().json(objectMapper.writeValueAsString(productRequest)));

    }

    @DisplayName("제품 목록 반환 요청을 처리한다.")
    @Test
    void list() throws Exception {
        List<ProductResponse> expected = Collections.singletonList(createProductResponse());
        when(mockProductService.list()).thenReturn(expected);
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }
}
