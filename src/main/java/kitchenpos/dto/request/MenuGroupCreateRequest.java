package kitchenpos.dto.request;

import kitchenpos.domain.MenuGroup;

public class MenuGroupCreateRequest {

    private String name;

    private MenuGroupCreateRequest() {
    }

    public MenuGroup toEntity() {
        return new MenuGroup(name);
    }

    public MenuGroupCreateRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
