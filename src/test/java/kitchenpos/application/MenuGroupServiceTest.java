package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class MenuGroupServiceTest extends ServiceTestContext {

    @Test
    void 메뉴_그룹을_생성할_수_있다() {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("menuGroup");

        // when
        MenuGroup created = menuGroupService.create(menuGroup);

        // then
        assertThat(created.getId()).isNotNull();
    }

    @Test
    void 모든_메뉴_그룹을_조회할_수_있다() {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("menuGroup");

        menuGroupService.create(menuGroup);

        // when
        List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).hasSize(2);
    }
}