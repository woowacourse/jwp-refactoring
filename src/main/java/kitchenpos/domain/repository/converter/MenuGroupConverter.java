package kitchenpos.domain.repository.converter;

import kitchenpos.domain.MenuGroup;
import kitchenpos.persistence.dto.MenuGroupDataDto;
import org.springframework.stereotype.Component;

@Component
public class MenuGroupConverter implements Converter<MenuGroup, MenuGroupDataDto> {

    @Override
    public MenuGroupDataDto entityToData(final MenuGroup menuGroup) {
        return new MenuGroupDataDto(menuGroup.getId(), menuGroup.getName());
    }

    @Override
    public MenuGroup dataToEntity(final MenuGroupDataDto menuGroupData) {
        return new MenuGroup(menuGroupData.getId(), menuGroupData.getName());
    }
}
