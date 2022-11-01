package kitchenpos.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;

public class MenusResponse {

    private List<MenuResponse> menuResponses;

    private MenusResponse() {
    }

    private MenusResponse(final List<MenuResponse> menuResponses) {
        this.menuResponses = menuResponses;
    }

    public static MenusResponse from(List<Menu> menu) {
        List<MenuResponse> menuResponses = menu.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());

        return new MenusResponse(menuResponses);
    }

    public List<MenuResponse> getMenuResponses() {
        return menuResponses;
    }
}
