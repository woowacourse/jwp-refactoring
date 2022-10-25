package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@ApplicationTest
class MenuGroupServiceTest {

    private final MenuGroupDao menuGroupDao;
    private final MenuGroupService menuGroupService;

    MenuGroupServiceTest(MenuGroupDao menuGroupDao, MenuGroupService menuGroupService) {
        this.menuGroupDao = menuGroupDao;
        this.menuGroupService = menuGroupService;
    }

    @Test
    void 메뉴그룹을_생성한다() {
        MenuGroup menuGroup = new MenuGroup("햄버거");

        MenuGroup actual = menuGroupService.create(menuGroup);
        assertThat(actual.getId()).isExactlyInstanceOf(Long.class);
    }

    @Test
    void 메뉴그룹을_모두_조회한다() {
        menuGroupDao.save(new MenuGroup("햄버거"));

        List<MenuGroup> menuGroups = menuGroupService.list();

        assertThat(menuGroups).hasSizeGreaterThanOrEqualTo(1);
    }
}
