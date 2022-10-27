package kitchenpos.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;

public class MenusResponse {

    private List<MenuResponse> menus;

    public MenusResponse() {
    }

    private MenusResponse(final List<MenuResponse> menus) {
        this.menus = menus;
    }

    public static MenusResponse of(final List<Menu> menus) {
        final List<MenuResponse> menuResponses = menus.stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
        return new MenusResponse(menuResponses);
    }

    public List<MenuResponse> getMenus() {
        return menus;
    }
}
