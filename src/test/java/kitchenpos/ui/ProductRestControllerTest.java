package kitchenpos.ui;

import static kitchenpos.fixture.ProductFixture.상품;
import static kitchenpos.fixture.ProductFixture.상품_등록_요청;
import static kitchenpos.fixture.ProductFixture.상품_등록_응답;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.request.ProductCreateRequest;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.domain.product.Product;
import kitchenpos.repositroy.ProductRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class ProductRestControllerTest extends ControllerTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void 상품을_성공적으로_등록하면_201응답과_생성된_상품_정보를_받는다() {
        // given
        final String name = "후라이드치킨";
        final BigDecimal price = BigDecimal.valueOf(1000);
        final ProductCreateRequest request = 상품_등록_요청(name, price);
        final RequestSpecification given = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(request);

        // when
        final ExtractableResponse<Response> result = given
                .when()
                .post("/api/products")
                .then().log().all()
                .extract();
        final ProductResponse responseBody = result.as(ProductResponse.class);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(result.statusCode()).isEqualTo(201);
            softly.assertThat(responseBody.getId()).isNotNull();
            softly.assertThat(responseBody.getProductName()).isEqualTo(name);
            softly.assertThat(responseBody.getProductPrice()).isEqualTo(price.setScale(2));
        });
    }

    @Test
    void 상품_등록_시_가격이_없다면_500_예외를_반환한다() {
        // given
        final ProductCreateRequest request = 상품_등록_요청("후라이드치킨", null);
        final RequestSpecification given = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(request);

        // when
        final ExtractableResponse<Response> result = given
                .when()
                .post("/api/products")
                .then().log().all()
                .extract();

        //then
        assertThat(result.statusCode()).isEqualTo(500);
    }

    @Test
    void 상품_등록_시_가격이_음수_면_500_예외를_반환한다() {
        // given
        final ProductCreateRequest request = 상품_등록_요청("후라이드치킨", BigDecimal.valueOf(-1000));
        final RequestSpecification given = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(request);

        // when
        final ExtractableResponse<Response> result = given
                .when()
                .post("/api/products")
                .then().log().all()
                .extract();

        //then
        assertThat(result.statusCode()).isEqualTo(500);
    }

    //TODO: 테스트 격리
    @Test
    void 등록된_모든_상품을_조회한다() {
        // given
        final Product product1 = productRepository.save(상품("후라이드치킨", BigDecimal.valueOf(1000)));
        final Product product2 = productRepository.save(상품("양념치킨", BigDecimal.valueOf(2000)));

        // when
        final ExtractableResponse<Response> result = RestAssured
                .when().get("/api/products")
                .then().log().all()
                .extract();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(result.statusCode()).isEqualTo(200);
            softly.assertThat(result.jsonPath().getList(".", ProductResponse.class))
                    .usingRecursiveComparison()
                    .isEqualTo(List.of(
                            상품_등록_응답(product1),
                            상품_등록_응답(product2))
                    );
        });
    }
}
