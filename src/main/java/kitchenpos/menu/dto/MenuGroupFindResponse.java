package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupFindResponse {
    private Long id;
    private String name;

    private MenuGroupFindResponse() {
    }

    public MenuGroupFindResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupFindResponse from(final MenuGroup menuGroup) {
        return new MenuGroupFindResponse(menuGroup.getId(), menuGroup.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
