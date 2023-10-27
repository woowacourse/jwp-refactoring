package kitchenpos.application.menu.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;

public class MenusResponse {

    private final List<MenuResponse> menus;

    private MenusResponse(final List<MenuResponse> menus) {
        this.menus = menus;
    }

    public static MenusResponse from(final List<Menu> menus) {
        List<MenuResponse> menuResponses = menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toUnmodifiableList());
        return new MenusResponse(menuResponses);
    }

    public List<MenuResponse> getMenus() {
        return menus;
    }
}
