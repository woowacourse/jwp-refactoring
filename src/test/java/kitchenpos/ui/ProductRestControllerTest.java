package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.common.fixture.RequestBody;
import kitchenpos.application.ProductService;
import kitchenpos.common.ControllerTest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(ProductRestController.class)
@DisplayName("ProductRestController 는 ")
class ProductRestControllerTest extends ControllerTest {

    @MockBean
    private ProductService productService;

    @DisplayName("제품을 생성한다.")
    @Test
    void createProduct() throws Exception {
        when(productService.create(any(Product.class))).thenReturn(DomainFixture.getProduct());

        final ResultActions resultActions = mockMvc.perform(post("/api/products")
                        .content(objectMapper.writeValueAsString(RequestBody.PRODUCT))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        resultActions.andDo(document("products/create",
                getRequestPreprocessor(),
                getResponsePreprocessor(),
                requestFields(
                        fieldWithPath("name").type(STRING).description("product name"),
                        fieldWithPath("price").type(NUMBER).description("product price")
                ),
                responseFields(
                        fieldWithPath("id").type(NUMBER).description("product id"),
                        fieldWithPath("name").type(STRING).description("product name"),
                        fieldWithPath("price").type(NUMBER).description("product price")
                )
        ));
    }

    @DisplayName("제품들을 조회한다.")
    @Test
    void getProducts() throws Exception {
        when(productService.list()).thenReturn(List.of(DomainFixture.getProduct()));

        final ResultActions resultActions = mockMvc.perform(get("/api/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        resultActions.andDo(document("products/get-products",
                getResponsePreprocessor(),
                responseFields(
                        fieldWithPath("[].id").type(NUMBER).description("product id"),
                        fieldWithPath("[].name").type(STRING).description("product name"),
                        fieldWithPath("[].price").type(NUMBER).description("product price")
                )
        ));
    }
}
