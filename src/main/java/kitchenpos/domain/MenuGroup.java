package kitchenpos.domain;

import kitchenpos.builder.MenuGroupBuilder;

public class MenuGroup {
    private Long id;
    private String name;

    public MenuGroup(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupBuilder builder() {
        return new MenuGroupBuilder();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
