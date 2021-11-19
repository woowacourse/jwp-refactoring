package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class MenuGroupServiceTest extends IntegrationTest {

    @DisplayName("메뉴 그룹을 생성한다")
    @Test
    void create() {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("MenuGroup");

        // when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertThat(savedMenuGroup.getId()).isNotNull();
        assertThat(savedMenuGroup.getName()).isEqualTo("MenuGroup");
    }

    @DisplayName("메뉴 그룹의 이름은 null일 수 없다")
    @Test
    void create_fail_menuGroupNameCannotBeNull() {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(null);

        // when, then
        assertThatCode(() -> menuGroupService.create(menuGroup))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("메뉴 그룹 목록을 조회한다")
    @Test
    void list() {
        List<MenuGroup> menuGroups = menuGroupService.list();

        assertThat(menuGroups).hasSize(4);
    }
}