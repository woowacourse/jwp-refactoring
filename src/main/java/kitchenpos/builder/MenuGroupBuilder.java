package kitchenpos.builder;

import kitchenpos.domain.MenuGroup;

public class MenuGroupBuilder {
    private Long id;
    private String name;

    public MenuGroupBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public MenuGroupBuilder name(String name) {
        this.name = name;
        return this;
    }

    public MenuGroup build() {
        return new MenuGroup(id, name);
    }
}
