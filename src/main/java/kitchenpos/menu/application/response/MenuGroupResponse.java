package kitchenpos.menu.application.response;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupResponse {

    private final Long id;
    private final String name;

    public MenuGroupResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse from(MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }

    public static List<MenuGroupResponse> fromAll(final List<MenuGroup> menuGroups) {
        return menuGroups.stream()
            .map(MenuGroupResponse::from)
            .collect(Collectors.toUnmodifiableList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
