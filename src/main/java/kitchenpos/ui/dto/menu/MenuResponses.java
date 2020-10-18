package kitchenpos.ui.dto.menu;

import kitchenpos.domain.Menu;

import java.util.List;
import java.util.stream.Collectors;

public class MenuResponses {

    private List<MenuResponse> menuResponses;

    public MenuResponses(final List<MenuResponse> menuResponses) {
        this.menuResponses = menuResponses;
    }

    public static MenuResponses from(List<Menu> menus) {
        return new MenuResponses(
                menus.stream()
                        .map(MenuResponse::from)
                        .collect(Collectors.toList())
        );
    }

    public List<MenuResponse> getMenuResponses() {
        return menuResponses;
    }
}
