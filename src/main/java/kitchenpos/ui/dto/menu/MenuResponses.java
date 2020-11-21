package kitchenpos.ui.dto.menu;

import java.util.List;

public class MenuResponses {

    private List<MenuResponse> menuResponses;

    protected MenuResponses() {
    }

    private MenuResponses(List<MenuResponse> menuResponses) {
        this.menuResponses = menuResponses;
    }

    public static MenuResponses from(List<MenuResponse> menuResponses) {
        return new MenuResponses(menuResponses);
    }

    public List<MenuResponse> getMenuResponses() {
        return menuResponses;
    }
}
