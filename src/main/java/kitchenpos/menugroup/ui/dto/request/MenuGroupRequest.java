package kitchenpos.menugroup.ui.dto.request;

import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupRequest {

    private String name;

    private MenuGroupRequest() {
    }

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public MenuGroup toEntity() {
        return MenuGroup.create(name);
    }

    public String getName() {
        return name;
    }
}
