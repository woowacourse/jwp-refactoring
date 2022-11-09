package kitchenpos.common;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import kitchenpos.menu.application.request.MenuGroupRequest;
import kitchenpos.menu.application.request.MenuRequest;
import kitchenpos.order.application.request.OrderRequest;
import kitchenpos.product.application.request.ProductCreateRequest;
import kitchenpos.table.application.request.OrderTableRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("classpath:truncate.sql")
public class AcceptanceTest {

    @Value("${local.server.port}")
    int port;

    protected static Long NO_ID = null;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    public ValidatableResponse post(String url, Object request) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().post(url)
            .then().log().all();
    }

    public ValidatableResponse get(String url) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get(url)
            .then().log().all();
    }

    public ValidatableResponse put(String url, Object request) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().put(url)
            .then().log().all();
    }

    public ValidatableResponse delete(String url) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete(url)
            .then().log().all();
    }

    protected long _메뉴등록_Id반환(final MenuRequest request) {
        return post("/api/menus", request).assertThat()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .body()
            .jsonPath()
            .getLong("id");
    }

    protected long _메뉴그룹등록_Id반환(final MenuGroupRequest request) {
        return post("/api/menu-groups", request).assertThat()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .body()
            .jsonPath()
            .getLong("id");
    }

    protected long _상품등록_Id반환(final ProductCreateRequest request) {
        return post("/api/products", request).assertThat()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .body()
            .jsonPath()
            .getLong("id");
    }

    protected long _테이블생성_Id반환(final OrderTableRequest request) {
        return post("/api/tables", request).assertThat()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .body()
            .jsonPath()
            .getLong("id");
    }

    protected long _주문생성_Id반환(final OrderRequest request) {
        return post("/api/orders", request).assertThat()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .body()
            .jsonPath()
            .getLong("id");
    }
}
