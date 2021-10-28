package kitchenpos.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

import static kitchenpos.application.Fixtures.MENU_GROUP;
import static org.assertj.core.api.Assertions.assertThat;

class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Test
    @DisplayName("메뉴 그룹 생성")
    void createTest() {

        // when
        menuGroupService.create(MENU_GROUP);

        // then
        assertThat(menuGroupDao.findById(1L).get()).isEqualTo(MENU_GROUP);
    }

    @Test
    @DisplayName("메뉴 그룹 목록 조회")
    void listTest() {

        // given
        menuGroupService.create(MENU_GROUP);

        // when
        final List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).contains(MENU_GROUP);
    }
}
