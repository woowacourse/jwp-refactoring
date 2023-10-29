package kitchenpos.menu.application.mapper;

import kitchenpos.menu.application.dto.response.MenuGroupResponse;
import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupMapper {

    public static MenuGroupResponse mapToMenuGroupResponse(final MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }
}
