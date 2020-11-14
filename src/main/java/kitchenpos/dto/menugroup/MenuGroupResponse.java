package kitchenpos.dto.menugroup;

import kitchenpos.domain.menu.MenuGroup;

public class MenuGroupResponse {
    private Long id;
    private String name;

    public MenuGroupResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse from(MenuGroup menuGroup) {
        Long id = menuGroup.getId();
        String name = menuGroup.getName();

        return new MenuGroupResponse(id, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
