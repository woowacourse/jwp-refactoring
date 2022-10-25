package kitchenpos.application;

import static kitchenpos.application.fixture.MenuGroupFixture.UNSAVED_MENU_GROUP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴그룹 생성")
    @Test
    void create() {
        MenuGroup savedMenuGroup = menuGroupService.create(UNSAVED_MENU_GROUP);
        List<MenuGroup> menuGroups = menuGroupService.list();

        assertThat(menuGroups).contains(savedMenuGroup);
    }

    @DisplayName("메뉴그룹 생성할때 이름이 없으면 예외가 발생한다.")
    @Test
    void create_Exception() {
        MenuGroup menuGroup = new MenuGroup(null);
        assertThatThrownBy(() -> menuGroupService.create(menuGroup))
                .isInstanceOf(Exception.class);
    }
}
