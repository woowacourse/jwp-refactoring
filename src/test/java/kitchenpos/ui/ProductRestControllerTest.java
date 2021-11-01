package kitchenpos.ui;

import kitchenpos.RestControllerTest;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;

import static kitchenpos.fixture.ProductFixture.createProduct;
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
        Product requestProduct = createProduct();
        when(mockProductService.create(any())).then(AdditionalAnswers.returnsFirstArg());
        mockMvc.perform(post("/api/products")
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestProduct))
                )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/products/" + requestProduct.getId()))
                .andExpect(content().json(objectMapper.writeValueAsString(requestProduct)));

    }

    @DisplayName("제품 목록 반환 요청을 처리한다.")
    @Test
    void list() throws Exception {
        List<Product> expected = Collections.singletonList(createProduct());
        when(mockProductService.list()).thenReturn(expected);
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }
}
