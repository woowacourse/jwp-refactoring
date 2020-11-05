package kitchenpos.ui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.application.fixture.ProductFixture.createProduct;
import static kitchenpos.application.fixture.ProductFixture.createProductRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductRestController.class)
class ProductRestControllerTest {
    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("상품을 생성한다")
    void create() throws Exception {
        Product request = createProductRequest("강정치킨", BigDecimal.valueOf(17000));
        byte[] content = objectMapper.writeValueAsBytes(request);
        given(productService.create(any(Product.class)))
                .willReturn(createProduct(3L, "강정치킨", BigDecimal.valueOf(17000)));

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("강정치킨"))
                .andExpect(jsonPath("$.price").value(17000));
    }

    @Test
    @DisplayName("전체 상품을 조회한다")
    void list() throws Exception {
        List<Product> persistedProducts = Arrays.asList(
                createProduct(1L, "치킨", BigDecimal.ONE),
                createProduct(2L, "마요", BigDecimal.TEN),
                createProduct(3L, "돈가", BigDecimal.valueOf(10000))
        );
        given(productService.list()).willReturn(persistedProducts);

        byte[] responseBody = mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        List<Product> result = objectMapper.readValue(responseBody, new TypeReference<List<Product>>() {
        });
        assertThat(result).usingRecursiveComparison().isEqualTo(persistedProducts);
    }
}