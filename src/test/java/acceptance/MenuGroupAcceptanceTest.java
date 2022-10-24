package acceptance;

import static fixture.MenuGroupFixtures.*;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import java.util.List;
import java.util.Map;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class MenuGroupAcceptanceTest extends AcceptanceTest {

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
                        tuple(두마리메뉴_그룹.id(), 두마리메뉴_그룹.이름()),
                        tuple(한마리메뉴_그룹.id(), 한마리메뉴_그룹.이름()),
                        tuple(순살파닭두마리메뉴_그룹.id(), 순살파닭두마리메뉴_그룹.이름()),
                        tuple(신메뉴_그룹.id(), 신메뉴_그룹.이름())
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
                        tuple(두마리메뉴_그룹.id(), 두마리메뉴_그룹.이름()),
                        tuple(한마리메뉴_그룹.id(), 한마리메뉴_그룹.이름()),
                        tuple(순살파닭두마리메뉴_그룹.id(), 순살파닭두마리메뉴_그룹.이름()),
                        tuple(신메뉴_그룹.id(), 신메뉴_그룹.이름()),
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
