package kitchenpos.ui.request;

import kitchenpos.domain.MenuGroup;

public class MenuGroupRequest {

    private final Long id;
    private final String name;

    public MenuGroupRequest(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public MenuGroup toMenuGroup() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);
        return menuGroup;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
