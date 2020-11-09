package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.config.TruncateDatabaseConfig;
import kitchenpos.domain.MenuGroup;

class MenuGroupServiceTest extends TruncateDatabaseConfig {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("MenuGroup 생성")
    @Test
    void create() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("신메뉴");

        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        assertAll(
            () -> assertThat(savedMenuGroup.getId()).isNotNull(),
            () -> assertThat(savedMenuGroup.getName()).isEqualTo("신메뉴")
        );
    }

    @DisplayName("MenuGroup 리스트 조회")
    @Test
    void list() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("신메뉴");
        menuGroupService.create(menuGroup);

        List<MenuGroup> menuGroupList = menuGroupService.list();

        assertAll(
            () -> assertThat(menuGroupList).hasSize(1),
            () -> assertThat(menuGroupList.get(0).getId()).isNotNull(),
            () -> assertThat(menuGroupList.get(0).getName()).isEqualTo("신메뉴")
        );

    }
}
