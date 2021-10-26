package kitchenpos.ui;

import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import kitchenpos.ui.factory.ProductFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest extends BaseWebMvcTest {

    @MockBean
    ProductService service;

    Product product1;
    Product product2;

    @BeforeEach
    void setUp() {
        product1 = ProductFactory.create(1L, "강정 치킨", new BigDecimal(17000));
        product2 = ProductFactory.create(2L, "100원 치킨", new BigDecimal(100));
    }

    @DisplayName("POST /api/products -> 상품을 등록한다.")
    @Test
    void create() throws Exception {
        // given
        Product requestProduct = ProductFactory.create(null, "강정 치킨", new BigDecimal(17000));
        String content = parseJson(requestProduct);

        given(service.create(any(Product.class)))
                .willReturn(product1);

        // when
        ResultActions actions = mvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(content));

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/products/1"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("강정 치킨")))
                .andExpect(jsonPath("$.price", is(17000)))
                .andDo(print());
    }

    @DisplayName("GET /api/products -> 상품 전체를 조회한다.")
    @Test
    void list() throws Exception {
        // given
        List<Product> products = Arrays.asList(product1, product2);

        given(service.list()).willReturn(products);

        // when
        ResultActions actions = mvc.perform(get("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"));

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("강정 치킨")))
                .andExpect(jsonPath("$[0].price", is(17000)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("100원 치킨")))
                .andExpect(jsonPath("$[1].price", is(100)))
                .andDo(print());
    }
}