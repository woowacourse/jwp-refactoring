package kitchenpos.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupCreateRequest {
    private Long id;
    private String name;

    protected MenuGroupCreateRequest() {
    }

    public MenuGroupCreateRequest(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public MenuGroupCreateRequest(MenuGroup menuGroup) {
        this(menuGroup.getId(), menuGroup.getName());
    }

    public MenuGroup toEntity() {
        return new MenuGroup(this.id, this.name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
