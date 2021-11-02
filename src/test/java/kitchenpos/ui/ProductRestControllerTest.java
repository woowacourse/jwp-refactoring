package kitchenpos.ui;

import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.ProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.ProductFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest extends ControllerTest {

    @MockBean
    private ProductService productService;

    @DisplayName("상품을 생성할 수 있다.")
    @Test
    void create() throws Exception {
        Long productId = 1L;
        ProductRequest request = new ProductRequest(PRODUCT_NAME, PRODUCT_PRICE);
        Product savedProduct = createProduct(productId);
        when(productService.create(any())).thenReturn(savedProduct);

        mockMvc.perform(post("/api/products")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/products/" + productId))
                .andExpect(content().json(objectMapper.writeValueAsString(savedProduct)));
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void list() throws Exception {
        List<Product> products = Arrays.asList(createProduct(1L), createProduct(2L));
        when(productService.list()).thenReturn(products);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(products)));
    }
}
