package kitchenpos.menu.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import kitchenpos.common.AcceptanceTest;
import kitchenpos.menu.ui.request.MenuGroupRequest;

public class MenuGroupAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("메뉴 그룹을 등록한다.")
    void createMenu() {
        // given
        MenuGroupRequest request = createRequest("세마리 메뉴");

        // when, then
        _메뉴그룹등록_Id반환(request);
    }

    @Test
    @DisplayName("전체 메뉴 그룹을 조회한다.")
    void findAll() {
        // given
        MenuGroupRequest request1 = createRequest("세마리 메뉴");
        MenuGroupRequest request2 = createRequest("네마리 메뉴");

        _메뉴그룹등록_Id반환(request1);
        _메뉴그룹등록_Id반환(request2);

        // when, then
        _메뉴그룹조회검증();
    }

    private void _메뉴그룹조회검증() {
        get("api/menu-groups").assertThat()
            .statusCode(HttpStatus.OK.value());
    }

    private MenuGroupRequest createRequest(final String name) {
        return new MenuGroupRequest(NO_ID, name);
    }
}
