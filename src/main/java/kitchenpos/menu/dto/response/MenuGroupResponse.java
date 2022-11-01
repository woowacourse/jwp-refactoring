package kitchenpos.menu.dto.response;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupResponse {

    private Long id;
    private String name;

    public MenuGroupResponse(MenuGroup menuGroup) {
        id = menuGroup.getId();
        name = menuGroup.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
