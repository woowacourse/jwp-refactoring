package kitchenpos.ui.product;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import kitchenpos.application.product.ProductService;
import kitchenpos.application.product.dto.ProductCreateRequest;
import kitchenpos.domain.Product;
import kitchenpos.helper.IntegrationTestHelper;
import kitchenpos.ui.product.dto.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static kitchenpos.fixture.ProductFixture.상품_생성_10000원;
import static kitchenpos.fixture.ProductFixture.상품_생성_요청;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
class ProductRestControllerAcceptanceTestFixture extends IntegrationTestHelper {

    @Autowired
    private ProductService productService;

    protected <T> ExtractableResponse 상품을_생성한다(final String url, final T request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(ContentType.JSON)
                .when()
                .post(url)
                .then().log().all()
                .extract();
    }

    protected void 상품이_성공적으로_생성된다(final ExtractableResponse response, final Product product) {
        ProductResponse result = response.as(ProductResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(result.getName()).isEqualTo(product.getName());
            softly.assertThat(result.getPrice().compareTo(product.getPrice()));
        });
    }

    protected ExtractableResponse 상품을_전체_조회한다(final String url) {
        return RestAssured.given().log().all()
                .when()
                .get(url)
                .then().log().all()
                .extract();
    }

    protected void 상품들이_성공적으로_조회된다(final ExtractableResponse response, final Product product) {
        List<ProductResponse> result = response.jsonPath()
                .getList("", ProductResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(result).hasSize(1);
            softly.assertThat(result.get(0).getName()).isEqualTo(product.getName());
            softly.assertThat(result.get(0).getPrice().compareTo(product.getPrice()));
        });
    }

    protected Product 상품_데이터를_생성한다() {
        Product product = 상품_생성_10000원();
        ProductCreateRequest request = 상품_생성_요청(product);

        return productService.create(request);
    }
}
