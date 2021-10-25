package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 인수 테스트")
public class MenuAcceptanceTest extends AcceptanceTest {
    @DisplayName("POST /api/menus")
    @Test
    void create() {
        // given
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2);

        // when
        Map<String, Object> params = new HashMap<>();
        params.put("name", "후라이드+후라이드");
        params.put("price", "19000.00");
        params.put("menuGroupId", "1");
        params.put("menuProducts", singletonList(menuProduct));

        // then
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/api/menus")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
        assertThat(response.body()).isNotNull();
    }

    @DisplayName("GET /api/menus")
    @Test
    void list() {
        // given - when

        // then
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("/api/menus")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        List<Menu> menus = convertBodyToList(response, Menu.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(menus).isNotNull();
        assertThat(menus).isNotEmpty();
    }
}
