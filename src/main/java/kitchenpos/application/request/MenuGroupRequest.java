package kitchenpos.application.request;

import kitchenpos.domain.MenuGroup;

public class MenuGroupRequest {

    private Long id;
    private String name;

    private MenuGroupRequest() {
    }

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

    public MenuGroup toEntity() {
        return new MenuGroup(name);
    }
}
