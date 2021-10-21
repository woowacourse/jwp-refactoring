package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 그룹 인수 테스트")
public class MenuGroupAcceptanceTest extends AcceptanceTest{
    @DisplayName("POST /api/menu-groups")
    @Test
    void createPost() {
        // given

        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", "추천메뉴");

        // then
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/api/menu-groups")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("GET /api/menu-groups")
    @Test
    void createGet() {
        // given - when
        // then
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("/api/menu-groups")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        List<MenuGroup> menuGroups = convertBodyToList(response, MenuGroup.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(menuGroups).isNotNull();
        assertThat(menuGroups).isNotEmpty();
    }
}
