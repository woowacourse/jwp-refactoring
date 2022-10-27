package kitchenpos.ui;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.ProductService;
import kitchenpos.application.dto.request.ProductRequest;
import kitchenpos.application.dto.response.ProductResponse;

@AutoConfigureMockMvc
@Transactional
@SpringBootTest
@DisplayName("Product API 테스트")
class ProductRestControllerTest {

    private static final String PRODUCT_NAME = "후라이드";
    private static final BigDecimal PRICE = new BigDecimal(15_000);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @DisplayName("상품을 등록한다")
    @Test
    void create() throws Exception {
        final ProductRequest request = new ProductRequest(PRODUCT_NAME, PRICE);
        final String body = objectMapper.writeValueAsString(request);

        final ProductResponse response = new ProductResponse(1L, PRODUCT_NAME, PRICE);
        BDDMockito.given(productService.create(any()))
            .willReturn(response);

        mockMvc.perform(post("/api/products")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/api/products/" + response.getId()))
            .andExpect(jsonPath("name", containsString(response.getName())))
            .andExpect(jsonPath("price", is(response.getPrice().intValue())))
        ;
    }

    @DisplayName("상품 목록을 조회한다")
    @Test
    void list() throws Exception {
        final ProductResponse response = new ProductResponse(1L, PRODUCT_NAME, PRICE);
        BDDMockito.given(productService.list())
                .willReturn(List.of(response));

        mockMvc.perform(get("/api/products"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
        ;
    }
}

