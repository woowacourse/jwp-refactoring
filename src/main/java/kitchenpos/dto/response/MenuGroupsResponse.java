package kitchenpos.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuGroup;

public class MenuGroupsResponse {

    private List<MenuGroupResponse> menuGroupResponses;

    private MenuGroupsResponse() {
    }

    private MenuGroupsResponse(final List<MenuGroupResponse> menuGroupResponses) {
        this.menuGroupResponses = menuGroupResponses;
    }

    public static MenuGroupsResponse of(List<MenuGroup> menuGroups) {
        List<MenuGroupResponse> menuGroupResponses = menuGroups.stream()
                .map(MenuGroupResponse::of)
                .collect(Collectors.toList());

        return new MenuGroupsResponse(menuGroupResponses);
    }

    public List<MenuGroupResponse> getMenuGroupResponses() {
        return menuGroupResponses;
    }
}
