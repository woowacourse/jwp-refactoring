package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.application.ServiceTest;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.response.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupServiceTest extends ServiceTest {

    @Test
    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    void createMenuGroup() {
        final String newMenuGroupName = "베스트메뉴";
        final MenuGroup request = new MenuGroup(newMenuGroupName);

        final MenuGroupResponse actual = menuGroupService.create(request);

        assertThat(actual.getName()).isEqualTo(newMenuGroupName);
    }

    @Test
    @DisplayName("메뉴 그룹 목록을 가져 온다.")
    void getMenuGroupList() {
        final String twoMenu = "두마리메뉴";
        final String oneMenu = "한마리메뉴";

        final List<MenuGroupResponse> menuGroups = menuGroupService.list();

        assertAll(
                () -> assertThat(menuGroups).hasSize(2),
                () -> assertThat(menuGroups.get(0).getName()).isEqualTo(twoMenu),
                () -> assertThat(menuGroups.get(1).getName()).isEqualTo(oneMenu)
        );
    }
}
