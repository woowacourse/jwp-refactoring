package kitchenpos.ui;

import kitchenpos.application.ProductService;
import kitchenpos.application.dto.request.CreateProductRequest;
import kitchenpos.application.dto.response.CreateProductResponse;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @BeforeEach
    void setUp() {
    }

    @Nested
    class 정상_요청_테스트 {

        @Test
        void 상품_생성() throws Exception {
            // given
            CreateProductResponse result = ProductFixture.RESPONSE.후라이드_치킨_16000원_생성_응답();
            given(productService.create(any(CreateProductRequest.class)))
                    .willReturn(result);

            // when & then
            mockMvc.perform(post("/api/products")
                            .contentType(APPLICATION_JSON)
                            .content("{" +
                                    "\"name\":\"" + result.getName() + "\"," +
                                    "\"price\":\"" + result.getPrice() + "\"" +
                                    "}")
                    )
                    .andExpectAll(
                            status().isCreated(),
                            header().exists("Location"),
                            jsonPath("id").value(result.getId()),
                            jsonPath("name").value(result.getName()),
                            jsonPath("price").value(result.getPrice())
                    );
        }

        @Test
        void 상품_목록_조회() throws Exception {
            // given
            ProductResponse response = ProductFixture.RESPONSE.후라이드_치킨_16000원_응답();
            given(productService.list())
                    .willReturn(List.of(response));

            // when & then
            mockMvc.perform(get("/api/products"))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$").isArray(),
                            jsonPath("$[0].id").value(response.getId()),
                            jsonPath("$[0].name").value(response.getName()),
                            jsonPath("$[0].price").value(response.getPrice())
                    );
        }
    }
}
