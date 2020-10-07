package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class MenuGroupAcceptanceTest extends AcceptanceTest {
    /**
     * Feature: 메뉴 그룹을 관리한다.
     *
     * When 메뉴 그룹을 등록한다. Then 메뉴그룹이 등록된다.
     *
     * When 모든 메뉴 그룹 목록을 조회한다. Then 메뉴 그룹 목록이 조회된다.
     */
    @Test
    @DisplayName("메뉴 그룹을 관리한다.")
    void manageMenuGroup() {
        MenuGroup menuGroup = createMenuGroup("세트 메뉴");

        assertThat(menuGroup.getId()).isNotNull();
        assertThat(menuGroup.getName()).isEqualTo("세트 메뉴");

        List<MenuGroup> menuGroups = findMenuGroups();
        assertThat(doesMenuExistInMenus("세트 메뉴", menuGroups)).isTrue();
    }

    private List<MenuGroup> findMenuGroups() {
        return given()
            .when()
                .get("/api/menu-groups")
            .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract()
                .jsonPath()
                .getList("", MenuGroup.class);
    }

    private boolean doesMenuExistInMenus(String menuGroupName, List<MenuGroup> menuGroups) {
        return menuGroups.stream()
            .anyMatch(product -> product
                .getName()
                .equals(menuGroupName));
    }
}
