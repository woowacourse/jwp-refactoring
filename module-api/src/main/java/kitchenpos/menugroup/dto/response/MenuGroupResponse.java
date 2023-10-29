package kitchenpos.menugroup.dto.response;

import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupResponse {

    private final Long id;
    private final String name;

    public MenuGroupResponse(MenuGroup menuGroup) {
        this(menuGroup.getId(), menuGroup.getName());
    }

    public MenuGroupResponse(Long id, String name) {
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
