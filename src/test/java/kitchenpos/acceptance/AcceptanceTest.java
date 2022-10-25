package kitchenpos.acceptance;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

    @Value("${local.server.port}")
    int port;

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

    protected long _메뉴등록_Id반환(final Menu menu) {
        return post("/api/menus", menu).assertThat()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .body()
            .jsonPath()
            .getLong("id");
    }

    protected long _메뉴그룹등록_Id반환(final MenuGroup menuGroup) {
        return post("/api/menu-groups", menuGroup).assertThat()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .body()
            .jsonPath()
            .getLong("id");
    }

    protected long _상품등록_Id반환(final Product product) {
        return post("/api/products", product).assertThat()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .body()
            .jsonPath()
            .getLong("id");
    }

    protected long _테이블생성_Id반환(final OrderTable orderTable) {
        return post("/api/tables", orderTable).assertThat()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .body()
            .jsonPath()
            .getLong("id");
    }

    protected long _주문생성_Id반환(final Order order) {
        return post("/api/orders", order).assertThat()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .body()
            .jsonPath()
            .getLong("id");
    }
}
