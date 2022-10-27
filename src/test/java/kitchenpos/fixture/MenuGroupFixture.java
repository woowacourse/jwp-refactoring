package kitchenpos.fixture;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.InMemoryMenuGroupDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static final Long 한마리메뉴 = 1L;
    public static final Long 두마리메뉴 = 2L;

    private final InMemoryMenuGroupDao inMemoryMenuGroupDao;
    private List<MenuGroup> fixtures;

    public MenuGroupFixture(final InMemoryMenuGroupDao inMemoryMenuGroupDao) {
        this.inMemoryMenuGroupDao = inMemoryMenuGroupDao;
    }

    public static MenuGroupFixture setUp() {
        MenuGroupFixture menuGroupFixture = new MenuGroupFixture(new InMemoryMenuGroupDao());
        menuGroupFixture.fixtures = menuGroupFixture.createMenuGroups();
        return menuGroupFixture;
    }

    public static MenuGroup createMenuGroup(final String menuGroupName) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(menuGroupName);
        return inMemoryMenuGroupDao.save(menuGroup);
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
        return inMemoryMenuGroupDao.save(menuGroup);
    }

    public MenuGroupDao getInMemoryMenuGroupDao() {
        return inMemoryMenuGroupDao;
    }
}
