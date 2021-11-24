package kitchenpos.menugroup.application.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menugroup.domain.MenuGroup;

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
