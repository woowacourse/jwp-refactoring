package kitchenpos.fixture;

import static kitchenpos.fixture.MenuGroupFixture.한마리메뉴;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.InMemoryMenuDao;
import kitchenpos.domain.Menu;

public class MenuFixture {

    public static final Long 맵슐랭 = 1L;
    public static final Long 허니콤보 = 2L;

    private final InMemoryMenuDao inMemoryMenuDao;
    private List<Menu> fixtures;

    public MenuFixture(final InMemoryMenuDao inMemoryMenuDao) {
        this.inMemoryMenuDao = inMemoryMenuDao;
    }

    public static MenuFixture setUp() {
        final MenuFixture menuFixture = new MenuFixture(new InMemoryMenuDao());
        menuFixture.fixtures = menuFixture.createMenus();
        return menuFixture;
    }

    public static Menu createMenu() {
        final Menu menu = new Menu();
        menu.setName("맵슐랭순살");
        menu.setPrice(new BigDecimal(20_000));
        menu.setMenuGroupId(한마리메뉴);
        return menu;
    }

    public static Menu createMenuByPrice(final BigDecimal price) {
        final Menu menu = new Menu();
        menu.setName("맵슐랭순살");
        menu.setPrice(price);
        menu.setMenuGroupId(한마리메뉴);
        return menu;
    }

    public static Menu createMenu(final Long menuGroupId) {
        final Menu menu = new Menu();
        menu.setName("맵슐랭순살");
        menu.setPrice(new BigDecimal(20_000));
        menu.setMenuGroupId(menuGroupId);
        return menu;
    }

    private List<Menu> createMenus() {
        return List.of(
                saveMenu(MenuGroupFixture.한마리메뉴, "맵슐랭", new BigDecimal(21_000)),
                saveMenu(MenuGroupFixture.한마리메뉴, "허니콤보", new BigDecimal(20_000))
        );
    }

    private Menu saveMenu(final Long menuGroupId, final String menuName, final BigDecimal price) {
        final Menu menu = new Menu();
        menu.setMenuGroupId(menuGroupId);
        menu.setName(menuName);
        menu.setPrice(price);
        return inMemoryMenuDao.save(menu);
    }

    public InMemoryMenuDao getInMemoryMenuDao() {
        return inMemoryMenuDao;
    }

    public List<Menu> getFixtures() {
        return Collections.unmodifiableList(fixtures);
    }
}
