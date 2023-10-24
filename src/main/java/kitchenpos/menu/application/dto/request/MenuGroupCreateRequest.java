package kitchenpos.menu.application.dto.request;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupCreateRequest {

    private String name;

    public MenuGroupCreateRequest(final String name) {
        this.name = name;
    }

    public MenuGroupCreateRequest() {
    }
 
    public MenuGroup toMenuGroup() {
        return new MenuGroup(name);
    }

    public String getName() {
        return name;
    }
}
