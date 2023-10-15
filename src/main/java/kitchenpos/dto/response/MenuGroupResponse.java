package kitchenpos.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuGroup;

public class MenuGroupResponse {

    private final Long menuId;
    private final String menuName;

    public MenuGroupResponse(Long menuId, String menuName) {
        this.menuId = menuId;
        this.menuName = menuName;
    }

    public static MenuGroupResponse of(MenuGroup menuGroup){
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }

    public static List<MenuGroupResponse> of(List<MenuGroup> menuGroups) {
        return menuGroups.stream()
            .map(MenuGroupResponse::of)
            .collect(Collectors.toList());
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getMenuName() {
        return menuName;
    }
}
