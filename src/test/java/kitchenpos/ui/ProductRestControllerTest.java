package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.annotation.ControllerTest;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ControllerTest
@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 상품을_저장한다() throws Exception {
        // given
        Product saved = new Product();
        saved.setId(1L);
        saved.setName("Chicken");
        saved.setPrice(BigDecimal.valueOf(1000));
        when(productService.create(any(Product.class))).thenReturn(saved);

        Product request = new Product();
        saved.setName("Chicken");
        saved.setPrice(BigDecimal.valueOf(1000));

        // when
        ResultActions result = mockMvc.perform(post("/api/products")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request))
        );

        // then
        result.andExpectAll(
                status().isCreated(),
                header().string("Location", "/api/products/" + saved.getId()),
                content().contentType(APPLICATION_JSON),
                content().json(objectMapper.writeValueAsString(saved))
        );
    }

    @Test
    void 상품_목록을_조회한다() throws Exception {
        // given
        List<Product> expected = List.of(new Product(), new Product());
        when(productService.list()).thenReturn(expected);

        // when
        ResultActions result = mockMvc.perform(get("/api/products"));

        // then
        result.andExpectAll(
                status().isOk(),
                content().contentType(APPLICATION_JSON),
                content().json(objectMapper.writeValueAsString(expected))
        );
    }
}
