package kitchenpos.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import kitchenpos.domain.MenuGroup;

public class MenuGroupAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("메뉴 그룹을 등록한다.")
    void createMenu() {
        // given
        MenuGroup menuGroup = new MenuGroup("세마리메뉴");

        // when, then
        _메뉴그룹등록_Id반환(menuGroup);
    }

    @Test
    @DisplayName("전체 메뉴 그룹을 조회한다.")
    void findAll() {
        // given
        MenuGroup menuGroup1 = new MenuGroup("세마리메뉴");
        MenuGroup menuGroup2 = new MenuGroup("네마리메뉴");

        _메뉴그룹등록_Id반환(menuGroup1);
        _메뉴그룹등록_Id반환(menuGroup2);

        // when, then
        _메뉴그룹조회검증();
    }

    private void _메뉴그룹조회검증() {
        get("api/menu-groups").assertThat()
            .statusCode(HttpStatus.OK.value());
    }
}
