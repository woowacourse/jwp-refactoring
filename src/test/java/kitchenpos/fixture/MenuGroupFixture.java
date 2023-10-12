package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

import java.util.List;

import static kitchenpos.fixture.FixtureUtil.listAllInDatabaseFrom;
import static kitchenpos.fixture.FixtureUtil.pushing;

@SuppressWarnings("NonAsciiCharacters")
public abstract class MenuGroupFixture {

    @InDatabase
    public static MenuGroup 두마리메뉴() {
        return pushing(new MenuGroup(), 1L, "두마리메뉴");
    }

    @InDatabase
    public static MenuGroup 한마리메뉴() {
        return pushing(new MenuGroup(), 2L, "한마리메뉴");
    }

    @InDatabase
    public static MenuGroup 순살파닭두마리메뉴() {
        return pushing(new MenuGroup(), 3L, "순살파닭두마리메뉴");
    }

    @InDatabase
    public static MenuGroup 신메뉴() {
        return pushing(new MenuGroup(), 4L, "신메뉴");
    }

    public static MenuGroup 일식메뉴() {
        return pushing(new MenuGroup(), 5L, "일식메뉴");
    }

    public static List<MenuGroup> listAllInDatabase() {
        return listAllInDatabaseFrom(MenuGroupFixture.class, MenuGroup.class);
    }
}
