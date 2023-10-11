package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    void 상품을_생성한다() throws Exception {
        // given
        Product createdProduct = new Product();
        createdProduct.setId(1L);
        createdProduct.setName("Test Product");

        // when
        when(productService.create(any(Product.class))).thenReturn(createdProduct);

        // then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(createdProduct)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/products/" + createdProduct.getId()))
                .andExpect(content().string(objectMapper.writeValueAsString(createdProduct)));
    }

    @Test
    void 상품을_전체_조회한다() throws Exception {
        // given
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");
        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");

        // when
        when(productService.list()).thenReturn(List.of(product1, product2));

        // then
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(List.of(product1, product2))));
    }
}
