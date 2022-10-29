package kitchenpos.acceptance;

import static kitchenpos.acceptance.RequestUtil.get;
import static kitchenpos.acceptance.RequestUtil.post;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.stream.Stream;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;

public class ProductAcceptanceTest extends AcceptanceTest {

    static ExtractableResponse<Response> 상품을_생성한다(final String name, final int price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));

        return post("/api/products", product);
    }

    static ExtractableResponse<Response> 모든_상품을_조회한다() {
        return get("/api/products");
    }

    @DisplayName("상품을 관리한다.")
    @TestFactory
    Stream<DynamicTest> manageProduct() {
        return Stream.of(
                dynamicTest("상품을 추가한다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 상품을_생성한다("강정치킨", 17_000);

                    // then
                    상태코드를_검증한다(response, HttpStatus.CREATED);
                    필드가_Null이_아닌지_검증한다(response, "id");
                }),
                dynamicTest("모든 상품을 조회한다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 모든_상품을_조회한다();

                    // then
                    상태코드를_검증한다(response, HttpStatus.OK);
                    리스트_길이를_검증한다(response, ".", 1);
                })
        );
    }
}
