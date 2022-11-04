package kitchenpos.dto.menu.mapper;

import java.util.List;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.dto.menu.response.MenuGroupResponse;

public interface MenuGroupDtoMapper {

    MenuGroupResponse toMenuGroupResponse(MenuGroup menuGroup);

    List<MenuGroupResponse> toMenuGroupResponses(List<MenuGroup> menuGroups);
}
