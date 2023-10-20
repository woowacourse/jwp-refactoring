package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.ProductService;
import kitchenpos.application.dto.ProductRequest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
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
        Product createdProduct = new Product(1L, "test product", BigDecimal.valueOf(1000));

        // when
        when(productService.create(any(ProductRequest.class))).thenReturn(createdProduct);

        // then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new ProductRequest("test product", BigDecimal.valueOf(1000)))))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/products/" + createdProduct.getId()))
                .andExpect(content().string(objectMapper.writeValueAsString(createdProduct)));
    }

    @Test
    void 상품을_전체_조회한다() throws Exception {
        // given
        Product product1 = new Product(1L, "product 1", BigDecimal.valueOf(1000));
        Product product2 = new Product(2L, "product 2", BigDecimal.valueOf(2000));

        // when
        when(productService.list()).thenReturn(List.of(product1, product2));

        // then
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(List.of(product1, product2))));
    }
}
