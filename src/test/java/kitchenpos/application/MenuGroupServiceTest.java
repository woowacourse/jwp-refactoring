package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@ApplicationTest
class MenuGroupServiceTest {

    private final MenuGroupService menuGroupService;

    MenuGroupServiceTest(MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @Test
    void 메뉴그룹을_생성한다() {
        MenuGroup menuGroup = new MenuGroup("햄버거");

        assertThat(menuGroupService.create(menuGroup)).isInstanceOf(MenuGroup.class);
    }

    @Test
    void 메뉴그룹을_모두_조회한다() {
        MenuGroup menuGroup = new MenuGroup("햄버거");
        menuGroupService.create(menuGroup);

        List<MenuGroup> menuGroups = menuGroupService.list();

        assertThat(menuGroups).hasSize(5);
    }
}
