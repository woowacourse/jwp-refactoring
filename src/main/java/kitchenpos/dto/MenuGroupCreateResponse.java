package kitchenpos.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupCreateResponse {
    private Long id;
    private String name;

    protected MenuGroupCreateResponse() {
    }

    public MenuGroupCreateResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public MenuGroupCreateResponse(MenuGroup menuGroup) {
        this(menuGroup.getId(), menuGroup.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
