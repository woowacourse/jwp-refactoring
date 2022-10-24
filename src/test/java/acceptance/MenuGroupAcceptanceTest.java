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

@AcceptanceTest
class MenuGroupAcceptanceTest {

    private static final long 두마리메뉴_ID = 1L;
    private static final String 두마리메뉴_이름 = "두마리메뉴";
    private static final long 한마리메뉴_ID = 2L;
    private static final String 한마리메뉴_이름 = "한마리메뉴";
    private static final long 순살파닭두마리메뉴_ID = 3L;
    private static final String 순살파닭두마리메뉴_이름 = "순살파닭두마리메뉴";
    private static final long 신메뉴_ID = 4L;
    private static final String 신메뉴_이름 = "신메뉴";

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void findMenuGroups() {
        // act
        List<MenuGroup> menuGroups = getMenuGroups();

        // assert
        assertThat(menuGroups)
                .extracting(MenuGroup::getId, MenuGroup::getName)
                .hasSize(4)
                .containsExactlyInAnyOrder(
                        tuple(두마리메뉴_ID, 두마리메뉴_이름),
                        tuple(한마리메뉴_ID, 한마리메뉴_이름),
                        tuple(순살파닭두마리메뉴_ID, 순살파닭두마리메뉴_이름),
                        tuple(신메뉴_ID, 신메뉴_이름)
                );
    }

    @Test
    @DisplayName("메뉴 그룹을 생성한다.")
    void createMenuGroups() {
        // arrange
        String name = "추천 메뉴";
        MenuGroup createdMenuGroup = createMenuGroup(name);

        // act
        List<MenuGroup> menuGroups = getMenuGroups();

        // assert
        assertThat(createdMenuGroup.getId()).isNotNull();
        assertThat(createdMenuGroup.getName()).isEqualTo(name);
        assertThat(menuGroups)
                .extracting(MenuGroup::getId, MenuGroup::getName)
                .hasSize(5)
                .containsExactlyInAnyOrder(
                        tuple(두마리메뉴_ID, 두마리메뉴_이름),
                        tuple(한마리메뉴_ID, 한마리메뉴_이름),
                        tuple(순살파닭두마리메뉴_ID, 순살파닭두마리메뉴_이름),
                        tuple(신메뉴_ID, 신메뉴_이름),
                        tuple(createdMenuGroup.getId(), createdMenuGroup.getName())
                );
    }

    private List<MenuGroup> getMenuGroups() {
        return RestAssured.given().log().all()
                .when().log().all()
                .get("/api/menu-groups")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath().getList(".", MenuGroup.class);
    }

    private MenuGroup createMenuGroup(String name) {
        return RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(Map.of("name", name))
                .when().log().all()
                .post("/api/menu-groups")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(MenuGroup.class);
    }
}
