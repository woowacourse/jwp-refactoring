package kitchenpos.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupRequest {

    private Long id;
    private String name;

    public MenuGroupRequest(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MenuGroup toMenuGroup() {
        return new MenuGroup(id, name);
    }
}
