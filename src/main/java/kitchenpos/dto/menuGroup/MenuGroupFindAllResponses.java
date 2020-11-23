package kitchenpos.dto.menuGroup;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.MenuGroup;

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
