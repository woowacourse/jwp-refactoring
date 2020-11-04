package kitchenpos.ui.dto.menu;

import java.util.List;

public class MenuResponses {

    private List<MenuResponse> menuResponses;

    public MenuResponses(final List<MenuResponse> menuResponses) {
        this.menuResponses = menuResponses;
    }

    public static MenuResponses from(List<MenuResponse> menus) {
        return new MenuResponses(menus);
    }

    public List<MenuResponse> getMenuResponses() {
        return menuResponses;
    }
}
