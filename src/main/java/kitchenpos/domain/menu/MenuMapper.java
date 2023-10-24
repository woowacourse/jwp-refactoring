package kitchenpos.domain.menu;

import kitchenpos.application.dto.request.CreateMenuRequest;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class MenuMapper {

    private final MenuProductMapper menuProductMapper;

    private MenuMapper(MenuProductMapper menuProductMapper) {
        this.menuProductMapper = menuProductMapper;
    }

    public Menu toMenu(final CreateMenuRequest createMenuRequest) {
        return Menu.builder()
                .name(createMenuRequest.getName())
                .price(createMenuRequest.getPrice())
                .menuGroupId(createMenuRequest.getMenuGroupId())
                .menuProducts(createMenuRequest.getMenuProducts()
                        .stream()
                        .map(menuProductMapper::toMenuProduct)
                        .collect(Collectors.toList()))
                .build();
    }
}
