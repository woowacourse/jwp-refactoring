package kitchenpos.dto.request;

import kitchenpos.domain.MenuGroup;

public class MenuGroupCreateRequest {

    private String name;

    private MenuGroupCreateRequest() {
    }

    public String getName() {
        return name;
    }

    public MenuGroup toMenuGroup() {
        return new MenuGroup(null, name);
    }
}
