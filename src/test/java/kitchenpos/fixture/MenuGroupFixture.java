package kitchenpos.fixture;

import kitchenpos.application.dto.request.MenuGroupCreateRequest;
import kitchenpos.application.dto.response.MenuGroupResponse;
import kitchenpos.domain.menu_group.MenuGroup;
import kitchenpos.domain.menu_group.MenuGroupName;

public class MenuGroupFixture {

    public static MenuGroup 메뉴_그룹(final String name) {
        final MenuGroupName menuGroupName = new MenuGroupName(name);
        return new MenuGroup(menuGroupName);
    }

    public static MenuGroupCreateRequest 메뉴_그룹_생성_요청(final String name) {
        return new MenuGroupCreateRequest(name);
    }

    public static MenuGroupResponse 메뉴_그룹_응답(final MenuGroup menuGroup) {
        return MenuGroupResponse.from(menuGroup);
    }
}
