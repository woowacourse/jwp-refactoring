package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    void createProduct() throws Exception {
        // given
        Product product = new Product();
        product.setId(1L);
        product.setName("치킨");
        product.setPrice(BigDecimal.valueOf(10000));

        given(productService.create(any())).willReturn(product);

        // when
        ResultActions actions = mockMvc.perform(post("/api/products")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(product))
        );

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/products/1"));
    }

    @Test
    void findProducts() throws Exception {
        // given
        Product productA = new Product();
        productA.setName("치킨");
        productA.setPrice(BigDecimal.valueOf(10000));

        Product productB = new Product();
        productB.setName("피자");
        productB.setPrice(BigDecimal.valueOf(8000));

        given(productService.list()).willReturn(List.of(productA, productB));

        // when
        ResultActions actions = mockMvc.perform(get("/api/products"));

        // then
        actions.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(productA, productB))));
    }
}
