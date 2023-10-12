package kitchenpos.acceptance.product;

import static kitchenpos.acceptance.AcceptanceSteps.given;
import static kitchenpos.acceptance.AcceptanceSteps.생성된_ID를_추출한다;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.assertj.core.data.Percentage;

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
                .then().log().all()
                .extract();
    }

    public static List<Product> 상품_목록_조회_요청을_보낸다() {
        ExtractableResponse<Response> extract = given()
                .get("/api/products")
                .then().log().all()
                .extract();
        return extract.as(new TypeRef<>() {
        });
    }

    public static Product 상품_정보(String 이름, double 가격) {
        Product product = new Product();
        product.setName(이름);
        product.setPrice(new BigDecimal(가격));
        return product;
    }

    public static void 상품_목록_조회_결과를_검증한다(List<Product> 응답, List<Product> 예상_결과) {
        assertThat(응답)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .ignoringFields("price")
                .isEqualTo(예상_결과);
        for (int i = 0; i < 응답.size(); i++) {
            assertThat(응답.get(i).getPrice())
                    .isCloseTo(예상_결과.get(i).getPrice(), Percentage.withPercentage(100));
        }
    }
}
