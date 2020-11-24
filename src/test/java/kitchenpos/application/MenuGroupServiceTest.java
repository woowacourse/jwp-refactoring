package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.domain.MenuGroup;

@SpringBootTest
class MenuGroupServiceTest {
    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void create() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("치킨");

        MenuGroup saved = menuGroupService.create(menuGroup);

        assertThat(saved.getId()).isNotNull();
    }

    @DisplayName("메뉴 그룹의 목록을 조회할 수 있다.")
    @Test
    void list() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("치킨");
        menuGroupService.create(menuGroup);

        List<MenuGroup> menuGroups = menuGroupService.list();

        assertThat(menuGroups).isNotEmpty();
    }
}