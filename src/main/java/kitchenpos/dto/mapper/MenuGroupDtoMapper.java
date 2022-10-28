package kitchenpos.dto.mapper;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.response.MenuGroupCreateResponse;
import kitchenpos.dto.response.MenuGroupResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MenuGroupDtoMapper {

    MenuGroupCreateResponse toMenuGroupCreateResponse(MenuGroup menuGroup);

    List<MenuGroupResponse> toMenuGroupResponses(List<MenuGroup> menuGroups);
}
