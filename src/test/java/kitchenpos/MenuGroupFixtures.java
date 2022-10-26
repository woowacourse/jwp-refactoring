package kitchenpos;

import kitchenpos.application.dto.MenuGroupRequest;
import kitchenpos.application.dto.MenuGroupResponse;

public class MenuGroupFixtures {

    public static MenuGroupResponse createMenuGroupResponse() {
        return new MenuGroupResponse(1L, "메뉴그룹");
    }

    public static MenuGroupRequest createMenuGroupRequest() {
        return new MenuGroupRequest("메뉴그룹");
    }
}
