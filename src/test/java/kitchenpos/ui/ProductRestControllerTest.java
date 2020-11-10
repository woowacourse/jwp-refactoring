package kitchenpos.ui;

import static kitchenpos.domain.DomainCreator.*;
import static org.hamcrest.core.StringContains.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;

@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest {
    private final String BASE_URL = "/api/products";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ProductService productService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilter(new CharacterEncodingFilter("UTF-8", true))
            .build();
    }

    @Test
    void create() throws Exception {
        Product product = createProduct("product", BigDecimal.valueOf(1000));
        product.setId(1L);
        String body = objectMapper.writeValueAsString(product);

        given(productService.create(any())).willReturn(product);

        mockMvc.perform(post(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(body)
        )
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(content().string(containsString("product")))
            .andExpect(content().string(containsString("1")))
            .andExpect(content().string(containsString("1000")));
    }

    @Test
    @DisplayName("상품의 목록을 불러올 수 있어야 한다.")
    void list() throws Exception {
        Product product1 = createProduct("product1", BigDecimal.valueOf(1000));
        product1.setId(1L);
        Product product2 = createProduct("product2", BigDecimal.valueOf(1000));
        product2.setId(2L);

        given(productService.list()).willReturn(Arrays.asList(product1, product2));

        mockMvc.perform(get(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("product1")))
            .andExpect(content().string(containsString("product2")));
    }
}
