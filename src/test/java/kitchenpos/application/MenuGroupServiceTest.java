package kitchenpos.application;

import static kitchenpos.constants.Constants.TEST_MENU_GROUP_NAME;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ui.dto.MenuGroupRequest;
import kitchenpos.ui.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupServiceTest extends KitchenPosServiceTest {

    @DisplayName("MenuGroup 생성 - 성공")
    @Test
    void create_Success() {
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest(TEST_MENU_GROUP_NAME);
        MenuGroupResponse createdMenuGroup = menuGroupService.create(menuGroupRequest);

        assertThat(createdMenuGroup.getId()).isNotNull();
        assertThat(createdMenuGroup.getName()).isEqualTo(TEST_MENU_GROUP_NAME);
    }

    @DisplayName("전체 MenuGroup 조회 - 성공")
    @Test
    void list_Success() {
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest(TEST_MENU_GROUP_NAME);
        MenuGroupResponse createdMenuGroup = menuGroupService.create(menuGroupRequest);

        List<MenuGroupResponse> menuGroups = menuGroupService.list();

        assertThat(menuGroups).isNotNull();
        assertThat(menuGroups).isNotEmpty();
        assertThat(menuGroups).contains(createdMenuGroup);

        List<Long> menuGroupIds = menuGroups.stream()
            .map(MenuGroupResponse::getId)
            .collect(Collectors.toList());

        assertThat(menuGroupIds).contains(createdMenuGroup.getId());
    }
}
