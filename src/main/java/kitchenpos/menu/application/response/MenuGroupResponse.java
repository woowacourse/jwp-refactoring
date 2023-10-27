package kitchenpos.menu.application.response;

import kitchenpos.menu.domain.MenuGroup;

import java.util.List;
import java.util.stream.Collectors;

public class MenuGroupResponse {

    private long id;
    private String name;

    public MenuGroupResponse(final long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse from(final MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName().getValue());
    }

    public static List<MenuGroupResponse> from(final List<MenuGroup> menuGroups) {
        return menuGroups.stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
