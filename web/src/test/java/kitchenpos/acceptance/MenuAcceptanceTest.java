package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.dto.menu.MenuUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 인수 테스트")
public class MenuAcceptanceTest extends DomainAcceptanceTest {
    @DisplayName("POST /api/menus")
    @Test
    void create() {
        // given
        Long productId = POST_SAMPLE_PRODUCT();
        Long menuGroupId = POST_SAMPLE_MENU_GROUP();

        MenuProductRequest menuProductRequest = MenuProductRequest.from(productId, 2L);
        MenuRequest menuRequest = MenuRequest.of(
                "후라이드+후라이드",
                BigDecimal.valueOf(19000.00),
                menuGroupId,
                singletonList(menuProductRequest)
        );

        // then
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuRequest)
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
        POST_SAMPLE_MENU();

        // then
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("/api/menus")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        List<MenuResponse> menus = convertBodyToList(response, MenuResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(menus).isNotNull();
        assertThat(menus).isNotEmpty();
    }

    @DisplayName("PUT /api/menus/{menuId}")
    @Test
    void update() {
        Long menuId = POST_SAMPLE_MENU();
        // given
        MenuUpdateRequest menuUpdateRequest = MenuUpdateRequest.of(
                "양념+양념",
                BigDecimal.valueOf(20000.00)
        );

        // then
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuUpdateRequest)
                .when().put("/api/menus/" + menuId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body()).isNotNull();
        MenuResponse menuResponse = response.as(MenuResponse.class);
        assertThat(menuResponse.getName()).isEqualTo(menuUpdateRequest.getName());
        assertThat(menuResponse.getPrice()).isEqualTo(menuUpdateRequest.getPrice());
    }
}
