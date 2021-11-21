package kitchenpos.menu.application.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;

public class MenuResponses {
    private final List<MenuResponse> responses;

    public MenuResponses(List<Menu> menus) {
        this.responses = menus.stream()
                .map(MenuResponse::new)
                .collect(Collectors.toList());
    }

    public List<MenuResponse> getResponses() {
        return responses;
    }
}
