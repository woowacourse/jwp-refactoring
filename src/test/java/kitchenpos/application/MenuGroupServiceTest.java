package kitchenpos.application;

import static kitchenpos.KitchenposTestHelper.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.domain.MenuGroup;

@SpringBootTest
class MenuGroupServiceTest {
    @Autowired
    private MenuGroupService menuGroupService;
    @Autowired
    private JdbcTemplateMenuGroupDao menuGroupDao;

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void create() {
        String name = "치킨";
        MenuGroup menuGroup = createMenuGroup(null, name);

        MenuGroup saved = menuGroupService.create(menuGroup);

        assertAll(
            () -> assertThat(saved.getId()).isNotNull(),
            () -> assertThat(saved.getName()).isEqualTo(name)
        );
    }

    @DisplayName("메뉴 그룹의 목록을 조회할 수 있다.")
    @Test
    void list() {
        MenuGroup menuGroup = createMenuGroup(null, "치킨");
        menuGroupDao.save(menuGroup);

        List<MenuGroup> menuGroups = menuGroupService.list();

        assertThat(menuGroups).hasSizeGreaterThanOrEqualTo(1);
    }
}