package kitchenpos.dto.menu.mapper;

import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.dto.menu.request.MenuGroupCreateRequest;
import org.springframework.stereotype.Component;

@Component
public class MenuGroupMapperImpl implements MenuGroupMapper {

    @Override
    public MenuGroup toMenuGroup(final MenuGroupCreateRequest menuGroupCreateRequest) {
        return new MenuGroup(null, menuGroupCreateRequest.getName());
    }
}
