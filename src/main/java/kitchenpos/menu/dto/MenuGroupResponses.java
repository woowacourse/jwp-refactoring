package kitchenpos.menu.dto;

import static java.util.stream.Collectors.*;

import java.util.List;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupResponses {
    private final List<MenuGroupResponse> menuGroupResponses;

    private MenuGroupResponses(List<MenuGroupResponse> menuGroupResponses) {
        this.menuGroupResponses = menuGroupResponses;
    }

    public static MenuGroupResponses from(List<MenuGroup> menuGroups) {
        return menuGroups.stream()
            .map(MenuGroupResponse::from)
            .collect(collectingAndThen(toList(), MenuGroupResponses::new));
    }

    public List<MenuGroupResponse> getMenuGroupResponses() {
        return menuGroupResponses;
    }
}
