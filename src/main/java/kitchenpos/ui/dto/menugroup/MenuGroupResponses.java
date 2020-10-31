package kitchenpos.ui.dto.menugroup;

import kitchenpos.domain.MenuGroup;

import java.util.List;
import java.util.stream.Collectors;

public class MenuGroupResponses {

    private List<MenuGroupResponse> menuGroupResponses;

    private MenuGroupResponses(final List<MenuGroupResponse> menuGroupResponses) {
        this.menuGroupResponses = menuGroupResponses;
    }

    public static MenuGroupResponses from(final List<MenuGroup> menuGroups) {
        return new MenuGroupResponses(menuGroups.stream()
            .map(MenuGroupResponse::from)
            .collect(Collectors.toList()));
    }

    public List<MenuGroupResponse> getMenuGroupResponses() {
        return menuGroupResponses;
    }
}
