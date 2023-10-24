package kitchenpos.mapper;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.menu.MenuCreateRequest;
import kitchenpos.dto.menu.MenuResponse;

public class MenuMapper {

    private MenuMapper() {
    }

    public static Menu toMenu(
            final MenuCreateRequest request,
            final MenuGroup menuGroup
    ) {
        return Menu.of(
                request.getName(),
                request.getPrice(),
                menuGroup
        );
    }

    public static MenuResponse toMenuResponse(
            final Menu menu,
            final List<MenuProduct> menuProducts
    ) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getMenuGroupId(),
                MenuProductMapper.toMenuProductResponses(menuProducts)
        );
    }

    public static List<MenuResponse> toMenuResponses(
            final List<Menu> menus
    ) {
        return menus.stream()
                .map(menu -> toMenuResponse(menu, menu.getMenuProducts()))
                .collect(Collectors.toList());
    }
}
