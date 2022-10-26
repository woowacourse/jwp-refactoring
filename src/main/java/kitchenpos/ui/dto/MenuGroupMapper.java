package kitchenpos.ui.dto;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.dto.request.MenuGroupCreateRequest;
import kitchenpos.ui.dto.response.MenuGroupCreateResponse;
import kitchenpos.ui.dto.response.MenuGroupResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MenuGroupMapper {

    @Mapping(target = "id", ignore = true)
    MenuGroup createRequestToMenuGroup(MenuGroupCreateRequest menuGroupCreateRequest);

    MenuGroupCreateResponse menuGroupToCreateResponse(MenuGroup menuGroup);

    List<MenuGroupResponse> menuGroupsToResponses(List<MenuGroup> menuGroups);
}
