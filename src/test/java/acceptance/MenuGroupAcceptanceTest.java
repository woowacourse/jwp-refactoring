package acceptance;

import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import java.util.List;
import java.util.Map;
import kitchenpos.Application;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(
        webEnvironment = WebEnvironment.RANDOM_PORT,
        classes = Application.class
)
@Sql("classpath:/db/migration/V1__Initialize_project_tables.sql")
public class MenuGroupAcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("메뉴 그룹 목록을 조회한다.")
    void getMenuGroups() {
        // given
        long menuGroupId1 = createMenuGroup("추천 메뉴");
        long menuGroupId2 = createMenuGroup("호호 메뉴");
        long menuGroupId3 = createMenuGroup("베루스 메뉴");
        long menuGroupId4 = createMenuGroup("라라 메뉴");

        // when
        List<MenuGroup> menuGroups = RestAssured.given().log().all()
                .when().log().all()
                .get("/api/menu-groups")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath().getList(".", MenuGroup.class);

        // then
        assertThat(menuGroups).extracting(MenuGroup::getId, MenuGroup::getName)
                .containsExactlyInAnyOrder(
                        tuple(menuGroupId1, "추천 메뉴"),
                        tuple(menuGroupId2, "호호 메뉴"),
                        tuple(menuGroupId3, "베루스 메뉴"),
                        tuple(menuGroupId4, "라라 메뉴")
                );
    }

    private long createMenuGroup(String name) {
        return RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(Map.of("name", name))
                .when().log().all()
                .post("/api/menu-groups")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().body().jsonPath().getLong("id");
    }
}
