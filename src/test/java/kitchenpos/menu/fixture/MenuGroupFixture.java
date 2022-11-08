package kitchenpos.menu.fixture;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup createMenuGroup(String name) {
        return MenuGroup.builder()
                .name(name)
                .build();
    }
}
