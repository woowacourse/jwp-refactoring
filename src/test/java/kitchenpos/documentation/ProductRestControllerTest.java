package kitchenpos.documentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.request.ProductCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

class ProductRestControllerTest extends DocumentationTest {
    private static final String PRODUCT_API_URL = "/api/products";

    @DisplayName("POST /api/products")
    @Test
    void create() {
        final var name = "까르보치킨";
        final var price = new BigDecimal("18000.00");
        given(productService.create(any()))
                .willReturn(new Product(1L, name, price));

        docsGiven
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ProductCreateRequest(name, price))
                .when().post(PRODUCT_API_URL)
                .then().log().all()
                .apply(document("products/create",
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("프로덕트 이름"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("프로덕트 가격")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("프로덕트 아이디"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("프로덕트 이름"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("프로덕트 가격")
                        )
                ))
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("GET /api/products")
    @Test
    void list() {
        given(productService.list())
                .willReturn(List.of(
                        new Product(1L, "까르보치킨", new BigDecimal("20000.00")),
                        new Product(2L, "짜장치킨", new BigDecimal("18000.00")))
                );

        docsGiven
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(PRODUCT_API_URL)
                .then().log().all()
                .apply(document("products/list",
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("프로덕트 아이디"),
                                fieldWithPath("[].name").type(JsonFieldType.STRING).description("프로덕트 이름"),
                                fieldWithPath("[].price").type(JsonFieldType.NUMBER).description("프로덕트 가격")
                        )
                ))
                .statusCode(HttpStatus.OK.value());
    }
}
