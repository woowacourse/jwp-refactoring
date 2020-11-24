package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.config.TruncateDatabaseConfig;
import kitchenpos.ui.dto.menugroup.MenuGroupRequest;
import kitchenpos.ui.dto.menugroup.MenuGroupResponse;
import kitchenpos.ui.dto.menugroup.MenuGroupResponses;

class MenuGroupServiceTest extends TruncateDatabaseConfig {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("MenuGroup 생성")
    @Test
    void create() {
        MenuGroupRequest request = new MenuGroupRequest("신메뉴");

        MenuGroupResponse savedMenuGroup = menuGroupService.create(request);

        assertAll(
            () -> assertThat(savedMenuGroup.getId()).isNotNull(),
            () -> assertThat(savedMenuGroup.getName()).isEqualTo("신메뉴")
        );
    }

    @DisplayName("MenuGroup 리스트 조회")
    @Test
    void list() {
        MenuGroupRequest request = new MenuGroupRequest("신메뉴");
        menuGroupService.create(request);

        MenuGroupResponses menuGroupResponses = menuGroupService.list();
        List<MenuGroupResponse> menuGroups = menuGroupResponses.getMenuGroupResponses();

        assertAll(
            () -> assertThat(menuGroups).hasSize(1),
            () -> assertThat(menuGroups.get(0).getId()).isNotNull(),
            () -> assertThat(menuGroups.get(0).getName()).isEqualTo("신메뉴")
        );

    }
}
