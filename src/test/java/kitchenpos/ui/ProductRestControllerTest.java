package kitchenpos.ui;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Collections;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.ProductRequest;
import kitchenpos.ui.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
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

    private static final long PRODUCT_ID = 1L;
    private static final String PRODUCT_NAME = "강정치킨";
    private static final BigDecimal PRODUCT_PRICE = BigDecimal.valueOf(17_000);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @DisplayName("상품 추가")
    @Test
    void create() throws Exception {
        Product product = new Product();
        product.setId(PRODUCT_ID);
        product.setName(PRODUCT_NAME);
        product.setPrice(ProductRestControllerTest.PRODUCT_PRICE);

        String requestBody = "{\n"
            + "  \"name\": \"" + product.getName() + "\",\n"
            + "  \"price\": " + product.getPrice() + "\n"
            + "}";

        given(productService.create(any(ProductRequest.class)))
            .willReturn(ProductResponse.of(product));

        ResultActions resultActions = mockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andDo(print());

        resultActions
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(header().exists(HttpHeaders.LOCATION))
            .andExpect(jsonPath("$.id", is(product.getId().intValue())))
            .andExpect(jsonPath("$.name", is(product.getName())))
            .andExpect(jsonPath("$.price", is(product.getPrice().intValue())))
            .andDo(print());
    }

    @DisplayName("상품 조회")
    @Test
    void list() throws Exception {
        Product product = new Product();
        product.setId(PRODUCT_ID);

        given(productService.list())
            .willReturn(ProductResponse.listOf(Collections.singletonList(product)));

        ResultActions resultActions = mockMvc.perform(get("/api/products")
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print());

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", is(product.getId().intValue())))
            .andDo(print());
    }
}
