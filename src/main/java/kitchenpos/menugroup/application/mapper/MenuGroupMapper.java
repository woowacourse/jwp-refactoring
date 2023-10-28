package kitchenpos.menugroup.application.mapper;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupCreateRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;

public class MenuGroupMapper {

    private MenuGroupMapper() {
    }

    public static MenuGroup toMenuGroup(
            final MenuGroupCreateRequest request
    ) {
        return new MenuGroup(request.getName());
    }

    public static MenuGroupResponse toMenuGroupResponse(
            final MenuGroup menuGroup
    ) {
        return new MenuGroupResponse(
                menuGroup.getId(),
                menuGroup.getName()
        );
    }

    public static List<MenuGroupResponse> toMenuGroupResponses(
            final List<MenuGroup> menuGroups
    ) {
        return menuGroups.stream()
                .map(MenuGroupMapper::toMenuGroupResponse)
                .collect(Collectors.toList());
    }
}
