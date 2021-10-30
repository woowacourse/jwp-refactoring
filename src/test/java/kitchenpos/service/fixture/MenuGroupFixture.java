package kitchenpos.service.fixture;

import static java.util.Collections.unmodifiableList;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.service.dao.TestMenuGroupDao;

public class MenuGroupFixture {

    public static final Long 두마리메뉴 = 1L;
    public static final Long 한마리메뉴 = 2L;
    public static final Long 순살파닭두마리메뉴 = 3L;
    public static final Long 신메뉴 = 4L;

    private final TestMenuGroupDao testMenuGroupDao;
    private List<MenuGroup> fixtures;

    private MenuGroupFixture(TestMenuGroupDao testMenuGroupDao) {
        this.testMenuGroupDao = testMenuGroupDao;
        createMenuGroup();
    }

    public static MenuGroupFixture createFixture() {
        MenuGroupFixture menuGroupFixture = new MenuGroupFixture(new TestMenuGroupDao());
        menuGroupFixture.fixtures = menuGroupFixture.createMenuGroup();

        return menuGroupFixture;
    }

    private List<MenuGroup> createMenuGroup() {
        return Arrays.asList(
            saveMenuGroup("두마리메뉴"),
            saveMenuGroup("한마리메뉴"),
            saveMenuGroup("순살파닭두마리메뉴"),
            saveMenuGroup("신메뉴")
        );
    }
    private MenuGroup saveMenuGroup(String menuGroupName) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(menuGroupName);
        return testMenuGroupDao.save(menuGroup);
    }

    public TestMenuGroupDao getTestMenuGroupDao() {
        return testMenuGroupDao;
    }

    public List<MenuGroup> getFixtures(){
        return unmodifiableList(fixtures);
    }
}
