package kitchenpos.dto.menu;

import kitchenpos.domain.menu.MenuGroup;

import java.util.List;
import java.util.stream.Collectors;

public class ListMenuGroupResponse {
    private final List<MenuGroupResponse> menuGroups;

    private ListMenuGroupResponse(final List<MenuGroupResponse> menuGroups) {
        this.menuGroups = menuGroups;
    }

    public static ListMenuGroupResponse from(final List<MenuGroup> menuGroups) {
        return new ListMenuGroupResponse(menuGroups.stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toList()));
    }

    public List<MenuGroupResponse> getMenuGroups() {
        return menuGroups;
    }
}
