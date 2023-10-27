package kitchenpos.menugroup.application.dto;

import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupCreateResponse {

    private final Long id;
    private final String name;

    public MenuGroupCreateResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupCreateResponse of(final MenuGroup menuGroup) {
        return new MenuGroupCreateResponse(menuGroup.getId(), menuGroup.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
