package kitchenpos.menu.application.mapper;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuResponse;

public class MenuMapper {

    private MenuMapper() {
    }

    public static Menu toMenu(
            final MenuCreateRequest request,
            final MenuGroup menuGroup,
            final List<MenuProduct> menuProducts
    ) {
        return Menu.of(
                request.getName(),
                request.getPrice(),
                menuGroup.getId(),
                menuProducts
        );
    }

    public static MenuResponse toMenuResponse(
            final Menu menu
    ) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice().getValue(),
                menu.getMenuGroupId(),
                MenuProductMapper.toMenuProductResponses(menu.getMenuProducts())
        );
    }

    public static List<MenuResponse> toMenuResponses(
            final List<Menu> menus
    ) {
        return menus.stream()
                .map(MenuMapper::toMenuResponse)
                .collect(Collectors.toList());
    }
}
