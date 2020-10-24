package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.domain.MenuGroup;

class MenuGroupServiceIntegrationTest extends ServiceIntegrationTest {
    @Autowired
    MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create() {
        MenuGroup twoChicken = new MenuGroup();
        twoChicken.setName("두마리메뉴");

        MenuGroup persist = menuGroupService.create(twoChicken);

        assertThat(persist).isEqualToIgnoringNullFields(twoChicken);
    }

    @DisplayName("메뉴 그룹 전체를 조회한다.")
    @Test
    void list() {
        List<MenuGroup> menuGroups = menuGroupService.list();

        assertAll(
            () -> assertThat(menuGroups).hasSize(4),
            () -> assertThat(menuGroups.get(0).getName()).isEqualTo("두마리메뉴"),
            () -> assertThat(menuGroups.get(1).getName()).isEqualTo("한마리메뉴"),
            () -> assertThat(menuGroups.get(2).getName()).isEqualTo("순살파닭두마리메뉴"),
            () -> assertThat(menuGroups.get(3).getName()).isEqualTo("신메뉴")
        );
    }
}