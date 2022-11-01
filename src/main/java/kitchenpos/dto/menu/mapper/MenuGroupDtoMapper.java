package kitchenpos.dto.menu.mapper;

import java.util.List;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.dto.menu.response.MenuGroupResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MenuGroupDtoMapper {

    MenuGroupResponse toMenuGroupResponse(MenuGroup menuGroup);

    List<MenuGroupResponse> toMenuGroupResponses(List<MenuGroup> menuGroups);
}
