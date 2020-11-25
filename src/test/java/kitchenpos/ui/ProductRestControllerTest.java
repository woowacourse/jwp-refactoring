package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.product.ProductService;
import kitchenpos.domain.product.Product;
import kitchenpos.ui.product.ProductRestController;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static kitchenpos.fixture.ProductFixture.createProductWithId;
import static kitchenpos.fixture.ProductFixture.createProductWithoutId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductRestController.class)
@AutoConfigureMockMvc
class ProductRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @DisplayName("Product 생성 요청")
    @Test
    void createOrderLineItems() throws Exception {
        Product product = createProductWithoutId();
        String content = new ObjectMapper().writeValueAsString(product);
        System.out.println(content);
        given(productService.create(any())).willReturn(createProductWithId(1L));

        mockMvc.perform(post("/api/products")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl("/api/products/1"));
    }

    @DisplayName("Product 전체 조회 요청")
    @Test
    void list() throws Exception {
        given(productService.list())
                .willReturn(Arrays.asList(createProductWithId(1L)));

        mockMvc.perform(get("/api/products")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Matchers.is(1)));
    }
}