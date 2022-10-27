package kitchenpos.support;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.MenuGroup;

public enum MenuGroupFixtures {

    MENU_GROUP1(1L, "두마리메뉴"),
    MENU_GROUP2(2L, "한마리메뉴"),
    MENU_GROUP3(3L, "순살파닭두마리메뉴"),
    MENU_GROUP4(4L, "신메뉴"),
    ;

    private Long id;
    private String name;

    MenuGroupFixtures(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public MenuGroup create() {
        return new MenuGroup(id, name);
    }

    public static List<MenuGroup> createAll() {
        return Arrays.asList(MENU_GROUP1.create(), MENU_GROUP2.create(), MENU_GROUP3.create(), MENU_GROUP4.create());
    }
}
