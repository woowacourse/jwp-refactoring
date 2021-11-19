package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.dto.menu.MenuGroupRequest;
import kitchenpos.dto.menu.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 그룹 인수 테스트")
public class MenuGroupAcceptanceTest extends DomainAcceptanceTest {
    @DisplayName("POST /api/menu-groups")
    @Test
    void createPost() {
        // given -  when
        MenuGroupRequest menuGroupRequest = MenuGroupRequest.from("추천메뉴");

        // then
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuGroupRequest)
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
        POST_SAMPLE_MENU_GROUP();

        // then
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("/api/menu-groups")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        List<MenuGroupResponse> menuGroups = convertBodyToList(response, MenuGroupResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(menuGroups).isNotNull();
        assertThat(menuGroups).isNotEmpty();
    }
}
