package kitchenpos.product.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import kitchenpos.TestFixtures;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.application.dto.ProductRequest;
import kitchenpos.product.application.dto.ProductResponse;
import kitchenpos.product.application.dto.ProductResponses;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("상품 api")
@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    void create() throws Exception {
        final String content = objectMapper.writeValueAsString(new ProductRequest());
        final Product product = TestFixtures.createProduct();
        when(productService.create(any())).thenReturn(new ProductResponse(product));

        final MockHttpServletResponse response = mockMvc.perform(post("/api/products")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertAll(
                () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.getHeader("Location")).isEqualTo("/api/products/" + product.getId())
        );
    }

    @Test
    void list() throws Exception {
        when(productService.list()).thenReturn(
                new ProductResponses(Collections.singletonList(TestFixtures.createProduct()))
        );

        final MockHttpServletResponse response = mockMvc.perform(get("/api/products"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}
