package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void create() {
        MenuGroup firstMenuGroup = new MenuGroup("1번 메뉴");

        MenuGroup menuGroup = menuGroupService.create(firstMenuGroup);

        assertThat(menuGroup).isNotNull();
    }

    @DisplayName("메뉴 그룹들을 조회할 수 있다.")
    @Test
    void list() {
        List<MenuGroup> menuGroups = menuGroupService.list();

        assertThat(menuGroups.size()).isEqualTo(4);
    }
}
