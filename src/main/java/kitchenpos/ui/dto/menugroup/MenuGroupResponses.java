package kitchenpos.ui.dto.menugroup;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.MenuGroup;

public class MenuGroupResponses {

    private List<MenuGroupResponse> menuGroupResponses;

    protected MenuGroupResponses() {
    }

    private MenuGroupResponses(List<MenuGroupResponse> menuGroupResponses) {
        this.menuGroupResponses = menuGroupResponses;
    }

    public static MenuGroupResponses from(List<MenuGroup> menuGroups) {
        return new MenuGroupResponses(menuGroups.stream()
            .map(MenuGroupResponse::from)
            .collect(Collectors.toList()));
    }

    public List<MenuGroupResponse> getMenuGroupResponses() {
        return Collections.unmodifiableList(menuGroupResponses);
    }
}
