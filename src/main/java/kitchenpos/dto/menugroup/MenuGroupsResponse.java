package kitchenpos.dto.menugroup;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuGroup;

public class MenuGroupsResponse {

    private final List<MenuGroupResponse> menuGroups;

    private MenuGroupsResponse(final List<MenuGroupResponse> menuGroups) {
        this.menuGroups = menuGroups;
    }

    public static MenuGroupsResponse from(final List<MenuGroup> menuGroups) {
        List<MenuGroupResponse> menuGroupResponses = menuGroups.stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toUnmodifiableList());
        return new MenuGroupsResponse(menuGroupResponses);
    }

    public List<MenuGroupResponse> getMenuGroups() {
        return menuGroups;
    }
}
