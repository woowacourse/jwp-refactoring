package kitchenpos.application.mapper;

import kitchenpos.application.dto.response.MenuGroupResponse;
import kitchenpos.domain.menu.MenuGroup;

public class MenuGroupMapper {

    public static MenuGroupResponse mapToMenuGroupResponse(final MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }
}
