package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.KitchenPosTestFixture;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = ProductRestController.class)
@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest extends KitchenPosTestFixture {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    private Product firstProduct;
    private Product secondProduct;

    @BeforeEach
    void setUp() {
        firstProduct = 상품을_저장한다(1L, "닭강정", BigDecimal.valueOf(1700));
        secondProduct = 상품을_저장한다(2L, "오뎅", BigDecimal.valueOf(500));
    }

    @Test
    void create() throws Exception {
        // given
        // when
        given(productService.create(any(Product.class))).willReturn(firstProduct);

        // then
        mvc.perform(post("/api/products")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstProduct)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(firstProduct.getId().intValue())))
                .andExpect(jsonPath("$.name", is(firstProduct.getName())))
                .andExpect(jsonPath("$.price", is(firstProduct.getPrice().intValue())));
    }

    @Test
    void list() throws Exception {
        // given
        List<Product> products = Arrays.asList(firstProduct, secondProduct);

        // when
        given(productService.list()).willReturn(products);

        // then
        mvc.perform(MockMvcRequestBuilders.get("/api/products")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(firstProduct.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(firstProduct.getName())))
                .andExpect(jsonPath("$[1].id", is(secondProduct.getId().intValue())))
                .andExpect(jsonPath("$[1].name", is(secondProduct.getName())))
        ;
    }
}