package kitchenpos.dto.menu.mapper;

import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.dto.menu.request.MenuGroupCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MenuGroupMapper {

    @Mapping(target = "id", ignore = true)
    MenuGroup toMenuGroup(MenuGroupCreateRequest menuGroupCreateRequest);
}
