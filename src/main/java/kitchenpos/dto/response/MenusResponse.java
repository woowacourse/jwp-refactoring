package kitchenpos.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;

public class MenusResponse {

    private final List<MenuResponse> menuResponses;

    public MenusResponse(final List<MenuResponse> menuResponses) {
        this.menuResponses = menuResponses;
    }

    public static MenusResponse from(final List<Menu> menus) {
        final List<MenuResponse> menuResponses = menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());

        return new MenusResponse(menuResponses);
    }

    public List<MenuResponse> getMenuResponses() {
        return menuResponses;
    }
}
