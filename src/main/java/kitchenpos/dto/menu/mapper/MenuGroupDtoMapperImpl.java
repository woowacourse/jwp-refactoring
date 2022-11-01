package kitchenpos.dto.menu.mapper;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.dto.menu.response.MenuGroupResponse;
import org.springframework.stereotype.Component;

@Component
public class MenuGroupDtoMapperImpl implements MenuGroupDtoMapper {

    @Override
    public MenuGroupResponse toMenuGroupResponse(final MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }

    @Override
    public List<MenuGroupResponse> toMenuGroupResponses(final List<MenuGroup> menuGroups) {
        return menuGroups.stream()
                .map(this::toMenuGroupResponse)
                .collect(Collectors.toList());
    }
}
