package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;

public class MenuGroupFixture {

    private static final Long ID = 1L;
    private static final String NAME = "MENU_GROUP_NAME";

    public static MenuGroup createMenuGroup() {
        MenuGroup menuGroup = new MenuGroup(NAME);
        menuGroup.setId(ID);
        return menuGroup;
    }

    public static MenuGroupRequest createMenuGroupRequest() {
        return new MenuGroupRequest(NAME);
    }

    public static MenuGroupResponse createMenuGroupResponse() {
        return new MenuGroupResponse(ID, NAME);
    }

    public static MenuGroupResponse createMenuGroupResponse(Long id, MenuGroupRequest request) {
        return new MenuGroupResponse(id, request.getName());
    }
}
