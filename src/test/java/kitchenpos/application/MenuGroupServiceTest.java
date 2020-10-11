package kitchenpos.application;

import static kitchenpos.constants.Constants.TEST_MENU_GROUP_NAME;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupServiceTest extends KitchenPosServiceTest {

    @DisplayName("MenuGroup 생성 - 성공")
    @Test
    void create_Success() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(TEST_MENU_GROUP_NAME);

        MenuGroup createdMenuGroup = menuGroupService.create(menuGroup);

        assertThat(createdMenuGroup.getId()).isNotNull();
        assertThat(createdMenuGroup.getName()).isEqualTo(TEST_MENU_GROUP_NAME);
    }

    @DisplayName("전체 MenuGroup 조회 - 성공")
    @Test
    void list_Success() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(TEST_MENU_GROUP_NAME);
        MenuGroup createdMenuGroup = menuGroupService.create(menuGroup);

        List<MenuGroup> menuGroups = menuGroupService.list();

        assertThat(menuGroups).isNotNull();
        assertThat(menuGroups).isNotEmpty();

        List<Long> menuGroupIds = menuGroups.stream()
            .map(MenuGroup::getId)
            .collect(Collectors.toList());

        assertThat(menuGroupIds).contains(createdMenuGroup.getId());
    }
}
