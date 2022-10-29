package kitchenpos.dto.mapper;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.response.MenuGroupResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MenuGroupDtoMapper {

    MenuGroupResponse toMenuGroupResponse(MenuGroup menuGroup);

    List<MenuGroupResponse> toMenuGroupResponses(List<MenuGroup> menuGroups);
}
