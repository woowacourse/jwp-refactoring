package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;

import java.util.List;
import java.util.stream.Collectors;

public class ListMenuResponse {
    private final List<MenuResponse> menus;


    private ListMenuResponse(final List<MenuResponse> menus) {
        this.menus = menus;
    }

    public static ListMenuResponse from(final List<Menu> menus) {
        return new ListMenuResponse(menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList()));
    }

    public List<MenuResponse> getMenus() {
        return menus;
    }
}
