package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.common.ServiceTest;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

@ServiceTest
class MenuGroupServiceTest {
    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @DisplayName("메뉴 그룹을 추가한다.")
    @Test
    void create() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("test_group");

        menuGroupDao.save(menuGroup);

        MenuGroup actual = menuGroupService.create(menuGroup);

        assertAll(
            () -> assertThat(actual).extracting(MenuGroup::getId).isNotNull(),
            () -> assertThat(actual).extracting(MenuGroup::getName).isEqualTo(menuGroup.getName())
        );
    }

    @DisplayName("메뉴 그룹 전체 목록을 조회한다.")
    @Test
    void list() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("Test");

        menuGroupDao.save(menuGroup);

        List<MenuGroup> actual = menuGroupService.list();

        assertThat(actual).hasSize(1);
    }
}