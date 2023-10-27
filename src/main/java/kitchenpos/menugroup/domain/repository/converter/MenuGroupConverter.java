package kitchenpos.menugroup.domain.repository.converter;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.persistence.dto.MenuGroupDataDto;
import kitchenpos.support.Converter;
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
