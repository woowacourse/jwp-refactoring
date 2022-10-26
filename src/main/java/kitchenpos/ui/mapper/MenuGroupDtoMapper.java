package kitchenpos.ui.mapper;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.dto.response.MenuGroupCreateResponse;
import kitchenpos.ui.dto.response.MenuGroupResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MenuGroupDtoMapper {

    MenuGroupCreateResponse toMenuGroupCreateResponse(MenuGroup menuGroup);

    List<MenuGroupResponse> toMenuGroupResponses(List<MenuGroup> menuGroups);
}
