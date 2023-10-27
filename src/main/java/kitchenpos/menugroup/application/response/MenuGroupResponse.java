package kitchenpos.menugroup.application.response;

import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupResponse {

    private Long id;
    private String name;

    private MenuGroupResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse from(MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
