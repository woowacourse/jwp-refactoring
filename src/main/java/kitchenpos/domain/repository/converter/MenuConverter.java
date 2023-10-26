package kitchenpos.domain.repository.converter;

import kitchenpos.domain.Menu;
import kitchenpos.persistence.dto.MenuDataDto;
import org.springframework.stereotype.Component;

@Component
public class MenuConverter implements Converter<Menu, MenuDataDto> {

    @Override
    public MenuDataDto entityToData(final Menu menu) {
        return new MenuDataDto(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId());
    }

    @Override
    public Menu dataToEntity(final MenuDataDto menuDataDto) {
        return Menu.builder(menuDataDto.getName(), menuDataDto.getPrice(), menuDataDto.getMenuGroupId())
                .id(menuDataDto.getId())
                .build();
    }
}
