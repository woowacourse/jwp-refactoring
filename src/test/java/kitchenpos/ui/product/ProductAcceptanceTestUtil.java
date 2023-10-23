package kitchenpos.ui.product;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.application.product.request.ProductCreateRequest;
import kitchenpos.ui.ControllerAcceptanceTestHelper;
import kitchenpos.ui.product.response.ProductResponse;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;

import java.util.List;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public abstract class ProductAcceptanceTestUtil extends ControllerAcceptanceTestHelper {

    protected ProductCreateRequest 상품_생성_요청() {
        return new ProductCreateRequest("커피", 1000L);
    }

    protected ExtractableResponse<Response> 상품을_생성한다(ProductCreateRequest 요청) {
        return RestAssured.given().body(요청)
                .contentType(ContentType.JSON)
                .when().post("/api/products")
                .then()
                .extract();
    }

    protected void 상품이_생성됨(ProductCreateRequest 요청, ExtractableResponse<Response> 응답) {
        ProductResponse response = 응답.as(ProductResponse.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(응답.statusCode()).isEqualTo(201);
            softly.assertThat(response.getName()).isEqualTo(요청.getName());
            softly.assertThat(response.getPrice()).isEqualByComparingTo(요청.getPrice());
        });
    }

    protected ExtractableResponse<Response> 상품_목록을_조회한다() {
        return RestAssured.given()
                .when().get("/api/products")
                .then()
                .extract();
    }

    protected void 상품이_조회됨(ExtractableResponse<Response> 응답) {
        List<ProductResponse> responses = 응답.jsonPath().getList(".", ProductResponse.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(응답.statusCode()).isEqualTo(200);
            softly.assertThat(responses).hasSize(1);
        });
    }
}
