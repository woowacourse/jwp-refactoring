package kitchenpos.ui;

import static kitchenpos.fixture.ProductFixture.CHICKEN;
import static kitchenpos.fixture.ProductFixture.CHICKEN_REQUEST;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kitchenpos.application.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
        // given
        given(productService.create(any())).willReturn(CHICKEN);

        // when & then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(CHICKEN_REQUEST)))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl("/api/products/1"));
    }

    @Test
    void list() throws Exception {
        // given
        given(productService.list()).willReturn(List.of(CHICKEN));

        // when & then
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(CHICKEN.getId()))
                .andExpect(jsonPath("$[0].name").value(CHICKEN.getName()))
                .andExpect(jsonPath("$[0].price").value(CHICKEN.getPrice()));
    }
}
