package kitchenpos.application.dtos;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuGroup;

public class MenuGroupResponses {
    private final List<MenuGroupResponse> menuGroupResponses;

    public MenuGroupResponses(List<MenuGroup> menuGroups) {
        this.menuGroupResponses = menuGroups.stream()
                .map(MenuGroupResponse::new)
                .collect(Collectors.toList());
    }

    public List<MenuGroupResponse> getMenuGroupResponses() {
        return menuGroupResponses;
    }
}
