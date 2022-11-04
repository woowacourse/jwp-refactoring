package kitchenpos.dto.menu.mapper;

import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.dto.menu.request.MenuGroupCreateRequest;

public interface MenuGroupMapper {

    MenuGroup toMenuGroup(MenuGroupCreateRequest menuGroupCreateRequest);
}
