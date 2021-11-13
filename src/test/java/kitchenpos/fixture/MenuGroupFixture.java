package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;

public class MenuGroupFixture {

    private static final String NAME = "MENU_GROUP_NAME";

    public static MenuGroup createMenuGroup() {
        return new MenuGroup(NAME);
    }

    public static MenuGroupRequest createMenuGroupRequest() {
        return new MenuGroupRequest(NAME);
    }

    public static MenuGroupResponse createMenuGroupResponse(Long id, String name) {
        return new MenuGroupResponse(id, name);
    }
    public static MenuGroupResponse createMenuGroupResponse(Long id, MenuGroupRequest request) {
        return new MenuGroupResponse(id, request.getName());
    }
}
