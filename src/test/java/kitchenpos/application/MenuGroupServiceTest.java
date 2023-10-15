package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다")
    @Test
    void create() {
        // given
        final int newMenuGroupId = menuGroupService.list().size() + 1;
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("test");

        // when
        final MenuGroup actual = menuGroupService.create(menuGroup);

        // then
        assertThat(actual.getId()).isEqualTo(newMenuGroupId);
    }

    @DisplayName("메뉴 그룹 목록을 조회한다")
    @Test
    void list() {
        // given & when
        final List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).hasSize(2);
    }
}
