package kitchenpos;

import java.util.Arrays;
import java.util.List;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {
    public static MenuGroup createMenuGroupWithoutId() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("추천메뉴");

        return menuGroup;
    }

    public static MenuGroup createMenuGroupWithId(final Long id) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName("추천메뉴");

        return menuGroup;
    }

    public static List<MenuGroup> createMenuGroups() {
        return Arrays.asList(createMenuGroupWithId(1L), createMenuGroupWithId(2L));
    }
}
