package kitchenpos.vo.menugroup;

import kitchenpos.domain.MenuGroup;

import java.util.List;
import java.util.stream.Collectors;

public class MenuGroupsResponse {
    private final List<MenuGroupResponse> menuGroupsResponse;

    private MenuGroupsResponse(List<MenuGroupResponse> menuGroupsResponse) {
        this.menuGroupsResponse = menuGroupsResponse;
    }

    public static MenuGroupsResponse of(List<MenuGroup> menuGroups) {
        List<MenuGroupResponse> menuGroupsResponse = menuGroups.stream()
                .map(MenuGroupResponse::of)
                .collect(Collectors.toList());

        return new MenuGroupsResponse(menuGroupsResponse);
    }

    public List<MenuGroupResponse> getMenuGroupsResponse() {
        return menuGroupsResponse;
    }
}
