package kitchenpos.service.fixture;

import kitchenpos.domain.MenuGroup;
import kitchenpos.service.dao.TestMenuGroupDao;

public class MenuGroupFixture {

    public static final Long 두마리메뉴 = 1L;
    public static final Long 한마리메뉴 = 2L;
    public static final Long 순살파닭두마리메뉴 = 3L;
    public static final Long 신메뉴 = 4L;

    private final TestMenuGroupDao testMenuGroupDao;

    private MenuGroupFixture(TestMenuGroupDao testMenuGroupDao) {
        this.testMenuGroupDao = testMenuGroupDao;
        createMenuGroup();
    }

    public static MenuGroupFixture createFixture(){
        MenuGroupFixture menuGroupFixture = new MenuGroupFixture(new TestMenuGroupDao());
        menuGroupFixture.createMenuGroup();
        return menuGroupFixture;
    }

    private void createMenuGroup() {
        saveMenuGroup("두마리메뉴");
        saveMenuGroup("한마리메뉴");
        saveMenuGroup("순살파닭두마리메뉴");
        saveMenuGroup("신메뉴");
    }

    public TestMenuGroupDao getTestMenuGroupDao(){
        return testMenuGroupDao;
    }

    private MenuGroup saveMenuGroup(String menuGroupName) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(menuGroupName);
        return testMenuGroupDao.save(menuGroup);
    }
}
