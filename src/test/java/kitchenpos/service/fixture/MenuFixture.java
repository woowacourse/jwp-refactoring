package kitchenpos.service.fixture;

import static java.util.Collections.unmodifiableList;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.service.dao.TestMenuDao;

public class MenuFixture {

    public static final Long 후라이드치킨 = 1L;
    public static final Long 양념치킨 = 2L;
    public static final Long 반반치킨 = 3L;
    public static final Long 통구이 = 4L;
    public static final Long 간장치킨 = 5L;
    public static final Long 순살치킨 = 6L;

    private List<Menu> fixtures;

    private final TestMenuDao testMenuDao;

    private MenuFixture(TestMenuDao testMenuDao) {
        this.testMenuDao = testMenuDao;
    }

    public static MenuFixture createFixture() {
        MenuFixture menuFixture = new MenuFixture(new TestMenuDao());
        menuFixture.fixtures = menuFixture.createMenu();
        return menuFixture;
    }

    public List<Menu> createMenu() {
        return Arrays.asList(
            saveMenuProduct(MenuGroupFixture.한마리메뉴, "후라이드치킨", new BigDecimal(16000)),
            saveMenuProduct(MenuGroupFixture.한마리메뉴, "양념치킨", new BigDecimal(16000)),
            saveMenuProduct(MenuGroupFixture.한마리메뉴, "반반치킨", new BigDecimal(16000)),
            saveMenuProduct(MenuGroupFixture.한마리메뉴, "통구이", new BigDecimal(16000)),
            saveMenuProduct(MenuGroupFixture.한마리메뉴, "간장치킨", new BigDecimal(17000)),
            saveMenuProduct(MenuGroupFixture.한마리메뉴, "순살치킨", new BigDecimal(17000))
        );
    }

    private Menu saveMenuProduct(Long menuGroupId, String menuName, BigDecimal price) {
        Menu menu = new Menu();
        menu.setMenuGroupId(menuGroupId);
        menu.setName(menuName);
        menu.setPrice(price);
        return testMenuDao.save(menu);
    }

    public TestMenuDao getTestMenuDao() {
        return testMenuDao;
    }

    public List<Menu> getFixtures(){
        return unmodifiableList(fixtures);
    }
}
