package kitchenpos.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.Fixtures;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.ui.ProductRestController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService productService;

    @DisplayName("product 생성")
    @Test
    void create() throws Exception {
        Product product = Fixtures.makeProduct();

        ObjectMapper objectMapper = new ObjectMapper();

        String content = objectMapper.writeValueAsString(product);

        given(productService.create(any(ProductRequest.class)))
            .willReturn(product);

        mvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isCreated());
    }

    @DisplayName("product 불러오기")
    @Test
    void list() throws Exception {
        List<Product> products = new ArrayList<>();

        given(productService.list())
            .willReturn(products);

        mvc.perform(get("/api/products")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
}
