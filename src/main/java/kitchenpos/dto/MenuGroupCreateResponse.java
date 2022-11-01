package kitchenpos.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupCreateResponse {
    private Long id;
    private String name;

    private MenuGroupCreateResponse() {
    }

    public MenuGroupCreateResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupCreateResponse from(final MenuGroup menuGroup) {
        return new MenuGroupCreateResponse(menuGroup.getId(), menuGroup.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
