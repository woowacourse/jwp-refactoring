package kitchenpos.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MenuGroupRestControllerTest extends ControllerTest {
    @Test
    @DisplayName("menu group 생성")
    void create() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("분식");

        ExtractableResponse<Response> response = postMenuGroup(menuGroup);
        MenuGroup savedMenuGroup = response.as(MenuGroup.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(savedMenuGroup.getId()).isNotNull();
    }

    @Test
    @DisplayName("모든 menu group 조회")
    void list() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("분식");
        postMenuGroup(menuGroup);

        MenuGroup menuGroup1 = new MenuGroup();
        menuGroup1.setName("양식");
        postMenuGroup(menuGroup1);

        ExtractableResponse<Response> response = getMenuGroups();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().as(List.class)).hasSize(2);
    }

    static ExtractableResponse<Response> postMenuGroup(MenuGroup menuGroup) {
        return RestAssured
                .given().log().all()
                .accept("application/json")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuGroup)
                .when().post("/api/menu-groups")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> getMenuGroups() {
        return RestAssured
                .given().log().all()
                .accept("application/json")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menu-groups")
                .then().log().all().extract();
    }
}
