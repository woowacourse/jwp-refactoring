package kitchenpos.domain.mapper;

import kitchenpos.application.dto.request.CreateMenuRequest;
import kitchenpos.domain.Menu;

import java.util.stream.Collectors;

public class MenuMapper {
    private MenuMapper() {
    }

    public static Menu toMenu(final CreateMenuRequest createMenuRequest) {
        return Menu.builder()
                .name(createMenuRequest.getName())
                .price(createMenuRequest.getPrice())
                .menuGroupId(createMenuRequest.getMenuGroupId())
                .menuProducts(createMenuRequest.getMenuProducts()
                        .stream()
                        .map(MenuProductMapper::toMenuProduct).collect(Collectors.toList()))
                .build();
    }
}
