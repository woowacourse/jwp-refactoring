package kitchenpos.fixture;

import application.dto.MenuGroupCreateRequest;
import application.dto.MenuGroupResponse;
import domain.MenuGroup;
import domain.MenuGroupName;

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
