package kitchenpos.fixture;

import kitchenpos.menugroup.MenuGroup;
import kitchenpos.ui.dto.MenuGroupRequest;

import java.util.List;

import static kitchenpos.fixture.FixtureUtil.listAllInDatabaseFrom;

@SuppressWarnings("NonAsciiCharacters")
public abstract class MenuGroupFixture {

    @InDatabase
    public static MenuGroup 두마리메뉴() {
        return new MenuGroup(1L, "두마리메뉴");
    }

    @InDatabase
    public static MenuGroup 한마리메뉴() {
        return new MenuGroup(2L, "한마리메뉴");
    }

    @InDatabase
    public static MenuGroup 순살파닭두마리메뉴() {
        return new MenuGroup(3L, "순살파닭두마리메뉴");
    }

    @InDatabase
    public static MenuGroup 신메뉴() {
        return new MenuGroup(4L, "신메뉴");
    }

    public static MenuGroup 일식메뉴() {
        return new MenuGroup(5L, "일식메뉴");
    }

    public static MenuGroupRequest 일식메뉴_REQUEST() {
        return new MenuGroupRequest("일식메뉴");
    }

    public static List<MenuGroup> listAllInDatabase() {
        return listAllInDatabaseFrom(MenuGroupFixture.class, MenuGroup.class);
    }
}
