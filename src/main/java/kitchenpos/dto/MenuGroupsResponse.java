package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuGroup;

public class MenuGroupsResponse {

    private List<MenuGroupResponse> menuGroups;

    private MenuGroupsResponse(List<MenuGroupResponse> menuGroups) {
        this.menuGroups = menuGroups;
    }

    public static MenuGroupsResponse from(List<MenuGroup> menuGroups) {
        return new MenuGroupsResponse(menuGroups.stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toList()));
    }

    public List<MenuGroupResponse> getMenuGroups() {
        return menuGroups;
    }
}
