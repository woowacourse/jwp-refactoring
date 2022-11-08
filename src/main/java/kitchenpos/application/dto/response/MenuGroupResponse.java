package kitchenpos.application.dto.response;

import kitchenpos.domain.menugroup.MenuGroup;

public class MenuGroupResponse {
    private Long id;
    private String name;

    private MenuGroupResponse() {
    }

    public MenuGroupResponse(final MenuGroup menuGroup) {
        this(menuGroup.getId(), menuGroup.getName());
    }

    public MenuGroupResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
