package kitchenpos.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuGroup;

public class MenuGroupsResponse {

    private List<MenuGroupResponse> menuGroups;

    public MenuGroupsResponse() {
    }

    private MenuGroupsResponse(List<MenuGroupResponse> menuGroups) {
        this.menuGroups = menuGroups;
    }

    public static MenuGroupsResponse of(final List<MenuGroup> menuGroups) {
        final List<MenuGroupResponse> menuGroupResponses = menuGroups.stream()
                .map(MenuGroupResponse::of)
                .collect(Collectors.toList());
        return new MenuGroupsResponse(menuGroupResponses);
    }

    public List<MenuGroupResponse> getMenuGroups() {
        return menuGroups;
    }
}
