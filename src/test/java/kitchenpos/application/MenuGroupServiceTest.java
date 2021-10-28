package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.request.MenuGroupRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("메뉴 그룹 서비스 테스트")
class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void create() {
        String menuGroupName = "menuGroup";
        MenuGroupRequest request = new MenuGroupRequest(menuGroupName);
        MenuGroupResponse createdMenuGroup = menuGroupService.create(request);

        assertAll(
            () -> assertThat(createdMenuGroup.getId()).isNotNull(),
            () -> assertThat(createdMenuGroup.getName()).isEqualTo(menuGroupName)
        );
    }

    @DisplayName("메뉴 그룹 리스트를 불러온다.")
    @Test
    void list() {
        MenuGroupRequest menuGroup1 = new MenuGroupRequest("menuGroup1");
        MenuGroupRequest menuGroup2 = new MenuGroupRequest("menuGroup2");
        menuGroupService.create(menuGroup1);
        menuGroupService.create(menuGroup2);

        List<MenuGroupResponse> menuGroups = menuGroupService.list();
        assertThat(menuGroups.size()).isEqualTo(2);
    }
}