package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.ProductFixtures;
import kitchenpos.application.ProductService;
import kitchenpos.application.dto.ProductResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class ProductRestControllerTest extends ControllerTest {

    private ProductService productService;

    @Autowired
    public ProductRestControllerTest(ProductService productService) {
        this.productService = productService;
    }

    @Test
    void createProduct() throws Exception {
        // given
        ProductResponse response = ProductFixtures.createProductResponse();
        given(productService.create(any())).willReturn(response);

        // when
        ResultActions actions = mockMvc.perform(post("/api/products")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(ProductFixtures.createProductRequest()))
        );

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/products/" + response.getId()));
    }

    @Test
    void findProducts() throws Exception {
        // given
        ProductResponse response = ProductFixtures.createProductResponse();
        given(productService.list()).willReturn(List.of(response));

        // when
        ResultActions actions = mockMvc.perform(get("/api/products"));

        // then
        actions.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(response))));
    }
}
