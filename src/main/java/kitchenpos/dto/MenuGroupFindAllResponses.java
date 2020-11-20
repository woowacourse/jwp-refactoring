package kitchenpos.dto;

import kitchenpos.domain.MenuGroup;

import java.util.List;
import java.util.stream.Collectors;

public class MenuGroupFindAllResponses {
    private List<MenuGroupFindAllResponse> menuGroupFindAllResponse;

    protected MenuGroupFindAllResponses() {
    }

    public MenuGroupFindAllResponses(List<MenuGroupFindAllResponse> menuGroupFindAllResponse) {
        this.menuGroupFindAllResponse = menuGroupFindAllResponse;
    }

    public static MenuGroupFindAllResponses from(List<MenuGroup> menuGroups) {
        List<MenuGroupFindAllResponse> menuGroupFindAllResponses = menuGroups.stream()
                .map(MenuGroupFindAllResponse::new)
                .collect(Collectors.toList());

        return new MenuGroupFindAllResponses(menuGroupFindAllResponses);
    }

    public List<MenuGroupFindAllResponse> getMenuGroupFindAllResponses() {
        return menuGroupFindAllResponse;
    }
}
