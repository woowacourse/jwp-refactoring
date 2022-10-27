package kitchenpos.dto.menugroup;

import kitchenpos.domain.MenuGroup;

public class MenuGroupCreateResponse {

    private final Long id;
    private final String name;

    public static MenuGroupCreateResponse from(MenuGroup menuGroup) {
        return new MenuGroupCreateResponse(menuGroup.getId(), menuGroup.getName());
    }

    private MenuGroupCreateResponse(Long id, String name) {
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
