package kitchenpos.menu.application.mapper;

import kitchenpos.menu.application.dto.request.MenuGroupCreateRequest;
import kitchenpos.menu.application.dto.response.MenuGroupResponse;
import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupMapper {

    private MenuGroupMapper() {
    }

    public static MenuGroup mapToMenuGroup(final MenuGroupCreateRequest menuGroupCreateRequest) {
        return new MenuGroup(menuGroupCreateRequest.getName());
    }

    public static MenuGroupResponse mapToResponse(final MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }
}
