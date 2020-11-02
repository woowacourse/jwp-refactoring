package kitchenpos.ui;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
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
import kitchenpos.ui.dto.ProductRequest;
import kitchenpos.ui.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest extends KitchenPosControllerTest {

    private static final ProductResponse PRODUCT;

    static {
        final long id = 1L;
        final String name = "강정치킨";
        final BigDecimal price = BigDecimal.valueOf(17_000);

        PRODUCT = ProductResponse.of(id, name, price);
    }

    @MockBean
    private ProductService productService;

    @DisplayName("상품 추가")
    @Test
    void create() throws Exception {
        ProductRequest productRequest = new ProductRequest(PRODUCT.getName(), PRODUCT.getPrice());

        given(productService.create(productRequest))
            .willReturn(PRODUCT);

        final ResultActions resultActions = mockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(productRequest)))
            .andDo(print());

        resultActions
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(header().exists(HttpHeaders.LOCATION))
            .andExpect(jsonPath("$.id", is(PRODUCT.getId().intValue())))
            .andExpect(jsonPath("$.name", is(PRODUCT.getName())))
            .andExpect(jsonPath("$.price", is(PRODUCT.getPrice().intValue())))
            .andDo(print());
    }

    @DisplayName("상품 조회")
    @Test
    void list() throws Exception {
        given(productService.list())
            .willReturn(Collections.singletonList(PRODUCT));

        final ResultActions resultActions = mockMvc.perform(get("/api/products")
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print());

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", is(PRODUCT.getId().intValue())))
            .andDo(print());
    }
}
