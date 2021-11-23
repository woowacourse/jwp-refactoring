package kitchenpos.ui.dto.response;

import kitchenpos.domain.MenuGroup;

import javax.persistence.Column;

public class MenuGroupResponse {

    private Long id;
    private String name;

    private MenuGroupResponse() {
    }

    private MenuGroupResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse create(MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
