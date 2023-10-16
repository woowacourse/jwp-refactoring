package kitchenpos.acceptance.product;

import static kitchenpos.acceptance.AcceptanceSteps.given;
import static kitchenpos.acceptance.AcceptanceSteps.생성된_ID를_추출한다;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import kitchenpos.domain.Product;

@SuppressWarnings("NonAsciiCharacters")
public class ProductAcceptanceSteps {

    public static Long 상품_등록후_생성된_ID를_가져온다(String 상품_이름, double 가격) {
        return 생성된_ID를_추출한다(상품_등록_요청을_보낸다(상품_이름, 가격));
    }

    public static ExtractableResponse<Response> 상품_등록_요청을_보낸다(String 상품_이름, double 가격) {
        Product product = new Product();
        product.setName(상품_이름);
        product.setPrice(new BigDecimal(가격));
        return given()
                .body(product)
                .post("/api/products")
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 상품_목록_조회_요청을_보낸다() {
        return given()
                .get("/api/products")
                .then()
                .log().headers()
                .extract();
    }
}
