package kitchenpos.application.dto.convertor;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.application.dto.request.MenuGroupRequest;
import kitchenpos.application.dto.response.MenuGroupResponse;
import kitchenpos.domain.MenuGroup;

public class MenuGroupConvertor {

    private MenuGroupConvertor() {
    }

    public static MenuGroup convertToMenuGroup(final MenuGroupRequest request) {
        return new MenuGroup(request.getName());
    }

    public static MenuGroupResponse convertToMenuGroupResponse(final MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }

    public static List<MenuGroupResponse> convertToMenuGroupResponses(final List<MenuGroup> menuGroups) {
        return menuGroups.stream()
            .map(menuGroup -> new MenuGroupResponse(menuGroup.getId(), menuGroup.getName()))
            .collect(Collectors.toUnmodifiableList());
    }
}
