package kitchenpos.acceptance.fixture;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import java.util.List;
import java.util.Map;
import kitchenpos.domain.MenuGroup;
import org.springframework.http.HttpStatus;

public class MenuGroupStepDefinition {

    public static long 메뉴_그룹을_생성한다(final String name) {
        return RestAssured.given().log().all()
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .body(Map.of("name", name))
            .when().log().all()
            .post("/api/menu-groups")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .extract().body().jsonPath().getLong("id");
    }

    public static List<MenuGroup> 메뉴_그룹을_조회한다() {
        return RestAssured.given().log().all()
            .when().log().all()
            .get("/api/menu-groups")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract().body().jsonPath().getList(".", MenuGroup.class);
    }
}
