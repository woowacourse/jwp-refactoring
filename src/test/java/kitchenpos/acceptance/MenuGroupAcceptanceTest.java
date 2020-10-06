package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
    }
}
