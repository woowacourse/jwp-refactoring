package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import kitchenpos.ControllerTest;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest extends ControllerTest {

    @MockBean
    private ProductService productService;

    @Test
    void 상품을_생성할_수_있다() throws Exception {
        // given
        Product product = new Product();
        when(productService.create(any(Product.class))).thenReturn(product);

        // when
        ResultActions response = mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)));

        // then
        response.andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(product)));
    }

    @Test
    void 상품_목록을_조회할_수_있다() throws Exception {
        // given
        Product product = new Product();
        when(productService.list()).thenReturn(Arrays.asList(product));

        // when
        ResultActions response = mockMvc.perform(get("/api/products"));

        // then
        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(Arrays.asList(product))));
    }
}
