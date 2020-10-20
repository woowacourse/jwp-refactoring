package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.ProductService;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.hamcrest.Matchers;
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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest {
    private static final String BASE_URL = "/api/products";

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @DisplayName("새로운 제품 생성")
    @Test
    void createTest() throws Exception {
        ProductRequest productRequest = new ProductRequest("강정치킨", BigDecimal.valueOf(17_000));
        String content = objectMapper.writeValueAsString(productRequest);
        ProductResponse productResponse = new ProductResponse(1L, "강정치킨", BigDecimal.valueOf(17_000));

        given(productService.create(any())).willReturn(productResponse);

        mockMvc.perform(
                post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.is(productResponse.getId().intValue())))
                .andExpect(jsonPath("$.name", Matchers.is(productRequest.getName())))
                .andExpect(jsonPath("$.price", Matchers.is(productRequest.getPrice().intValue())));
    }

    @DisplayName("저장된 모든 제품 출력")
    @Test
    void listTest() throws Exception {
        List<ProductResponse> productResponses = Arrays.asList(
                new ProductResponse(1L),
                new ProductResponse(2L),
                new ProductResponse(3L)
        );

        given(productService.list()).willReturn(productResponses);

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$.[0].id", Matchers.is(1)));
    }
}
