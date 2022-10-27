package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.dao.InMemoryMenuGroupDao;
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
        menuGroupDao = new InMemoryMenuGroupDao();
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
        final MenuGroup menuGroup1 = MenuGroupFixture.createMenuGroup("한마리메뉴");
        final MenuGroup menuGroup2 = MenuGroupFixture.createMenuGroup("두마리메뉴");
        menuGroupDao.save(menuGroup1);
        menuGroupDao.save(menuGroup2);

        final List<MenuGroup> menuGroups = menuGroupService.list();

        assertAll(
                () -> assertThat(menuGroups).hasSize(2),
                () -> assertThat(menuGroups.get(0).getName()).isEqualTo("한마리메뉴"),
                () -> assertThat(menuGroups.get(0).getId()).isNotNull(),
                () -> assertThat(menuGroups.get(1).getName()).isEqualTo("두마리메뉴"),
                () -> assertThat(menuGroups.get(1).getName()).isNotNull()
        );
    }
}
