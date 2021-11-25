package kitchenpos.menu.domain.dto.response;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupResponse {

    private Long id;
    private String name;

    private MenuGroupResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    protected MenuGroupResponse() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static MenuGroupResponse toDTO(MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }
}
