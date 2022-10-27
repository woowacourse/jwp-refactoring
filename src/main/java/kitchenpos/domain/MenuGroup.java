package kitchenpos.domain;

import kitchenpos.dto.MenuGroupRequest;

public class MenuGroup {

    private final Long id;
    private final String name;

    public MenuGroup(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public MenuGroup(String name) {
        this(null, name);
    }

    public static MenuGroup from(MenuGroupRequest menuGroupRequest) {
        return new MenuGroup(menuGroupRequest.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
