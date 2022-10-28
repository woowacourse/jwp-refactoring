package kitchenpos.application.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupResponse {
    private Long id;
    private String name;

    public MenuGroupResponse(MenuGroup menuGroup) {
        this(menuGroup.getId(), menuGroup.getName());
    }

    public MenuGroupResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
}
