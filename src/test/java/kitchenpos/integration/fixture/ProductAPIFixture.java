package kitchenpos.integration.fixture;

import static io.restassured.RestAssured.given;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.product.application.dto.request.ProductCreateRequest;
import kitchenpos.product.application.dto.response.ProductResponse;
import org.springframework.http.MediaType;
import java.math.BigDecimal;
import java.util.List;

public class ProductAPIFixture {

    public static final String DEFAULT_PRODUCT_NAME = "product";
    public static final BigDecimal DEFAULT_PRODUCT_PRICE = BigDecimal.valueOf(1000);

    public static ExtractableResponse<Response> createProduct(final ProductCreateRequest request) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .body(request)
                .post("/api/products")
                .then()
                .log().all()
                .extract();
    }

    public static ProductResponse createProductAndReturnResponse(final ProductCreateRequest request) {
        return createProduct(request).as(ProductResponse.class);
    }

    public static ProductResponse createDefaultProduct() {
        final ProductCreateRequest request = new ProductCreateRequest(DEFAULT_PRODUCT_NAME, DEFAULT_PRODUCT_PRICE);
        return createProductAndReturnResponse(request);
    }

    public static List<ProductResponse> listProducts() {
        final ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/products")
                .then()
                .log().all()
                .extract();
        return response.as(new TypeRef<>() {
        });
    }
}
