package kitchenpos.common.builder;

import kitchenpos.domain.MenuGroup;

public class MenuGroupBuilder {

    private String name;

    public MenuGroupBuilder name(final String name) {
        this.name = name;
        return this;
    }

    public MenuGroup build() {
        return new MenuGroup(name);
    }
}
