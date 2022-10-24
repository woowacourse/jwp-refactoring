package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

class MenuGroupServiceTest extends ServiceTest{

    @Autowired
    protected MenuGroupService menuGroupService;
    @Autowired
    protected MenuGroupDao menuGroupDao;
    @Test
    @DisplayName("메뉴 그룹을 저장한다")
    void create() {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("test");

        // when
        MenuGroup createdMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertAll(
            () -> assertThat(createdMenuGroup).isNotNull(),
            () -> assertThat(createdMenuGroup.getName()).isEqualTo("test")
        );
    }

    @Test
    @DisplayName("메뉴 그룹을 조회한다")
    void list() {
        // given
        int default_data_size = 4;

        // when
        List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertAll(
            () -> assertThat(menuGroups).hasSize(default_data_size)
        );
    }
}
