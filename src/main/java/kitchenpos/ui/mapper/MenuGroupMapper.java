package kitchenpos.ui.mapper;

import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.dto.request.MenuGroupCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MenuGroupMapper {

    @Mapping(target = "id", ignore = true)
    MenuGroup toMenuGroup(MenuGroupCreateRequest menuGroupCreateRequest);
}
