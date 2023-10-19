package support.fixture;

import kitchenpos.domain.menu_group.MenuGroup;

public class MenuGroupBuilder {

    private static int sequence = 1;

    private String name;

    public MenuGroupBuilder() {
        this.name = "메뉴그룹" + sequence;

        sequence++;
    }

    public MenuGroupBuilder setName(final String name) {
        this.name = name;
        return this;
    }

    public MenuGroup build() {
        return new MenuGroup(name);
    }
}
