package kitchenpos.menu.dto;

import static java.util.stream.Collectors.*;

import java.util.List;

import kitchenpos.menu.domain.Menu;

public class MenuResponses {

    private final List<MenuResponse> menuResponses;

    private MenuResponses(List<MenuResponse> menuResponses) {
        this.menuResponses = menuResponses;
    }

    public static MenuResponses from(List<Menu> menus) {
        return menus.stream()
            .map(MenuResponse::from)
            .collect(collectingAndThen(toList(), MenuResponses::new));
    }

    public List<MenuResponse> getMenuResponses() {
        return menuResponses;
    }
}
