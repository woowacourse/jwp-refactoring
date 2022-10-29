package kitchenpos.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupResponse {

    private Long id;
    private String name;

    private MenuGroupResponse() {
    }

    public MenuGroupResponse(final MenuGroup menuGroup) {
        this.id = menuGroup.getId();
        this.name = menuGroup.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
