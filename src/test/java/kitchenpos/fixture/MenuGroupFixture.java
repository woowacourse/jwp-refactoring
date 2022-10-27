package kitchenpos.fixture;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.InMemoryMenuGroupDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static final Long 한마리메뉴 = 1L;
    public static final Long 두마리메뉴 = 2L;

    private final MenuGroupDao menuGroupDao;
    private List<MenuGroup> fixtures;

    public MenuGroupFixture(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    public static MenuGroupFixture setUp() {
        MenuGroupFixture menuGroupFixture = new MenuGroupFixture(new InMemoryMenuGroupDao());
        menuGroupFixture.fixtures = menuGroupFixture.createMenuGroups();
        return menuGroupFixture;
    }

    public static MenuGroup createMenuGroup(final String menuGroupName) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(menuGroupName);
        return menuGroup;
    }

    private List<MenuGroup> createMenuGroups() {
        return List.of(
                saveMenuGroup("한마리메뉴"),
                saveMenuGroup("두마리메뉴")
        );
    }

    private MenuGroup saveMenuGroup(final String menuGroupName) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(menuGroupName);
        return menuGroupDao.save(menuGroup);
    }

    public MenuGroupDao getMenuGroupDao() {
        return menuGroupDao;
    }

    public List<MenuGroup> getFixtures() {
        return fixtures;
    }
}
