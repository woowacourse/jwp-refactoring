package kitchenpos.fixtures;

import kitchenpos.menugroup.service.dto.MenuGroupRequest;
import kitchenpos.menugroup.service.dto.MenuGroupResponse;
import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupFixtures {

    private static final long MENU_GROUP_ID = 1L;
    private static final String MENU_GROUP_NAME = "기본 메뉴 그룹";

    public static MenuGroupRequest createMenuGroupRequest(String name) {
        return new MenuGroupRequest(name);
    }

    public static MenuGroupRequest createMenuGroupRequest() {
        return createMenuGroupRequest(MENU_GROUP_NAME);
    }

    public static MenuGroup createMenuGroup(Long id, String name) {
        return new MenuGroup(id, name);
    }

    public static MenuGroup createMenuGroup() {
        return createMenuGroup(MENU_GROUP_ID, MENU_GROUP_NAME);
    }

    public static MenuGroupResponse createMenuGroupResponse(MenuGroup menuGroup) {
        return MenuGroupResponse.of(menuGroup);
    }
}
