package kitchenpos.ui.request;

import kitchenpos.domain.MenuGroup;

public class MenuGroupCreateRequest {

    private String name;

    public MenuGroupCreateRequest(final String name) {
        this.name = name;
    }

    public MenuGroupCreateRequest() {
    }

    public MenuGroup toMenuGroup() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }

    public String getName() {
        return name;
    }
}
