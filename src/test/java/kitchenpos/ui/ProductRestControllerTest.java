package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.ProductService;
import kitchenpos.application.dto.ProductRequest;
import kitchenpos.domain.Product;
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
        long id = 1L;
        String name = "치킨";
        BigDecimal price = BigDecimal.valueOf(10000);
        given(productService.create(any())).willReturn(new Product(id, name, price));

        // when
        ResultActions actions = mockMvc.perform(post("/api/products")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(new ProductRequest(name, price)))
        );

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/products/" + id));
    }

    @Test
    void findProducts() throws Exception {
        // given
        Product productA = new Product("치킨", BigDecimal.valueOf(10000));
        Product productB = new Product("피자", BigDecimal.valueOf(8000));

        given(productService.list()).willReturn(List.of(productA, productB));

        // when
        ResultActions actions = mockMvc.perform(get("/api/products"));

        // then
        actions.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(productA, productB))));
    }
}
