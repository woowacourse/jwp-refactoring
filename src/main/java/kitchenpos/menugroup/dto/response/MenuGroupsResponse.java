package kitchenpos.menugroup.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupsResponse {

    private List<MenuGroupResponse> menuGroupResponses;

    private MenuGroupsResponse() {
    }

    private MenuGroupsResponse(final List<MenuGroupResponse> menuGroupResponses) {
        this.menuGroupResponses = menuGroupResponses;
    }

    public static MenuGroupsResponse from(List<MenuGroup> menuGroups) {
        List<MenuGroupResponse> menuGroupResponses = menuGroups.stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toList());

        return new MenuGroupsResponse(menuGroupResponses);
    }

    public List<MenuGroupResponse> getMenuGroupResponses() {
        return menuGroupResponses;
    }
}
