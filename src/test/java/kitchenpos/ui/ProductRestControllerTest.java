package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L)
                .name("후라이드")
                .price(BigDecimal.valueOf(16000L))
                .build();
    }

    @Nested
    class 정상_요청_테스트 {

        @Test
        void 상품_생성() throws Exception {
            // given
            given(productService.create(any(Product.class)))
                    .willReturn(product);

            // when & then
            mockMvc.perform(post("/api/products")
                            .contentType(APPLICATION_JSON)
                            .content("{" +
                                    "\"name\":\"후라이드\"," +
                                    "\"price\":16000" +
                                    "}")
                    )
                    .andExpectAll(
                            status().isCreated(),
                            header().exists("Location"),
                            jsonPath("id").value(product.getId()),
                            jsonPath("name").value(product.getName()),
                            jsonPath("price").value(product.getPrice().intValue())
                    );
        }

        @Test
        void 상품_목록_조회() throws Exception {
            // given
            given(productService.list())
                    .willReturn(List.of(product));

            // when & then
            mockMvc.perform(get("/api/products"))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$").isArray(),
                            jsonPath("$[0].id").value(product.getId()),
                            jsonPath("$[0].name").value(product.getName()),
                            jsonPath("$[0].price").value(product.getPrice().intValue())
                    );
        }
    }
}
