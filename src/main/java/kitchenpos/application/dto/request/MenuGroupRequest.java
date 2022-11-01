package kitchenpos.application.dto.request;

import kitchenpos.domain.MenuGroup;

public class MenuGroupRequest {

    private Long id;
    private String name;

    public MenuGroupRequest(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupRequest of(final MenuGroup menuGroup) {
        return new MenuGroupRequest(menuGroup.getId(), menuGroup.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
