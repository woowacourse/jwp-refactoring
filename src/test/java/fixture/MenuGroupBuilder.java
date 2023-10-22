package fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupBuilder {
    private Long id;
    private String name;

    public static MenuGroupBuilder init() {
        final MenuGroupBuilder builder = new MenuGroupBuilder();
        builder.id = null;
        builder.name = "메뉴";
        return builder;
    }

    public MenuGroupBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public MenuGroupBuilder name(String name) {
        this.name = name;
        return this;
    }

    public MenuGroup build() {
        return new MenuGroup(
                this.id,
                this.name
        );
    }
}
