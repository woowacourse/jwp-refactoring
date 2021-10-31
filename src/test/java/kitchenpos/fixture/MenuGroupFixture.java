package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MenuGroupFixture {
    public static final MenuGroup 두마리메뉴;
    public static final MenuGroup 한마리메뉴;
    public static final MenuGroup 순살파닭두마리메뉴;
    public static final MenuGroup 신메뉴;

    static {
        두마리메뉴 = newInstance(1L, "두마리메뉴");
        한마리메뉴 = newInstance(2L, "한마리메뉴");
        순살파닭두마리메뉴 = newInstance(3L, "순살파닭두마리메뉴");
        신메뉴 = newInstance(4L, "신메뉴");
    }

    public static List<MenuGroup> menuGroups() {
        return Arrays.asList(두마리메뉴, 한마리메뉴, 순살파닭두마리메뉴, 신메뉴);
    }

    public static List<String> menuGroupNames() {
        return menuGroups().stream()
                .map(MenuGroup::getName)
                .collect(Collectors.toList());
    }

    private static MenuGroup newInstance(Long id, String name) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);
        return menuGroup;
    }
}
