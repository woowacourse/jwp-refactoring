package kitchenpos.dto.menugroup;

import kitchenpos.domain.menu.MenuGroup;

public class MenuGroupCreateRequest {
    private String name;

    public MenuGroupCreateRequest() {
    }

    public MenuGroupCreateRequest(String name) {
        this.name = name;
    }

    public MenuGroup toMenuGroup() {
        return MenuGroup.from(this.name);
    }

    public String getName() {
        return name;
    }
}
