package kitchenpos;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class ProductAcceptanceTest extends ApplicationTest {

    private Product 기본_상품 = new Product();

    @BeforeEach
    void setUp() {
        super.setUp();
        기본_상품.setName("포츈치킨");
        기본_상품.setPrice(BigDecimal.valueOf(7777));
    }

    @DisplayName("상품을 추가하는데 성공하면 201 응답을 받는다.")
    @Test
    void createProduct() {
        //when
        ExtractableResponse<Response> response = 상품_추가(기본_상품);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("잘못된 가격으로 상품을 추가하면 500 응답을 받는다.")
    @Test
    void wrongPriceCreateProduct() {

        //given
        기본_상품.setPrice(BigDecimal.valueOf(-1));

        //when
        ExtractableResponse<Response> response = 상품_추가(기본_상품);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("전체 상품 조회에 성공하면, 200 응답을 받는다.")
    @Test
    void getProducts(){
        ExtractableResponse<Response> response = 전체_상품_조회();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 상품_추가(final Product product) {
        return RestAssured
            .given().log().all()
            .body(product)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .post("/api/products")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> 전체_상품_조회() {
        return RestAssured
            .given().log().all()
            .when().get("/api/products")
            .then().log().all()
            .extract();
    }
}
