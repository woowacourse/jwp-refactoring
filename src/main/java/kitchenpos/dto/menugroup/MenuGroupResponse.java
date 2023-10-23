package kitchenpos.dto.menugroup;

import kitchenpos.domain.MenuGroup;

public class MenuGroupResponse {

    private Long id;

    private String name;

    public MenuGroupResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
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
