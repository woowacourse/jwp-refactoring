package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.fixture.MenuGroupFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupServiceTest {

    private MenuGroupDao menuGroupDao;
    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        menuGroupDao = MenuGroupFixture.setUp()
                .getMenuGroupDao();
        menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @Test
    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    void createMenuGroup() {
        final String newMenuGroupName = "베스트메뉴";
        final MenuGroup menuGroup = MenuGroupFixture.createMenuGroup(newMenuGroupName);

        final MenuGroup persistedMenuGroup = menuGroupService.create(menuGroup);

        assertThat(persistedMenuGroup.getName()).isEqualTo(newMenuGroupName);
    }

    @Test
    @DisplayName("메뉴 그룹 목록을 가져 온다.")
    void getMenuGroupList() {
        final List<MenuGroup> expectedMenuGroups = MenuGroupFixture.setUp().getFixtures();

        final List<MenuGroup> menuGroups = menuGroupService.list();

        assertAll(
                () -> assertThat(menuGroups).hasSameSizeAs(expectedMenuGroups),
                () -> assertThat(menuGroups)
                        .usingRecursiveComparison()
                        .isEqualTo(expectedMenuGroups)
        );
    }
}
