package kitchenpos.support.fixture.dto;

import kitchenpos.menu.application.dto.MenuGroupResponse;
import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupDtoFixture {

    public static MenuGroupResponse 메뉴_그룹_생성_응답(MenuGroup menuGroup) {
        return MenuGroupResponse.toResponse(menuGroup);
    }
}
