package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.dto.request.CreateProductRequest;
import kitchenpos.dto.response.CreateProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;

import static kitchenpos.fixture.ProductFixture.REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
class ProductTest extends AcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 상품을_생성한다() {
        // given
        CreateProductRequest request = REQUEST.후라이드_치킨_16000원();

        // when
        ExtractableResponse<Response> response = 상품_생성(request);

        // then
        assertThat(response.header("Location"))
                .isNotBlank();
    }


    @Test
    void 상품을_목록을_조회한다() {
        // given
        CreateProductRequest request = REQUEST.후라이드_치킨_16000원();
        상품_생성(request);

        // when & then
        ExtractableResponse<Response> response = RestAssured.given()
                .when().get("/api/products")
                .then().log().all()
                .statusCode(200)
                .extract();

        assertSoftly(softly -> {
            softly.assertThat(response.body().jsonPath().getList(".", CreateProductResponse.class)).hasSize(1);
            softly.assertThat(response.body().jsonPath().getLong("[0].id")).isNotNull();
            softly.assertThat(response.body().jsonPath().getString("[0].name")).isEqualTo("후라이드치킨");
            softly.assertThat(response.body().jsonPath().getString("[0].price")).contains(request.getPrice());
        });
    }

    private ExtractableResponse<Response> 상품_생성(CreateProductRequest request) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/api/products")
                .then().log().all()
                .statusCode(201)
                .extract();
    }
}
