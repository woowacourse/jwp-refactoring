package kitchenpos.menu.application.dto;

import kitchenpos.menu.domain.Menu;

public class MenuResponse {

    private final Long id;
    private final String name;
    private final int price;
    private final Long menuGroupId;

    public MenuResponse(Long id, String name, int price, Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public static MenuResponse toResponse(Menu menu) {
        int price = menu.getPrice().getValue().intValue();
        return new MenuResponse(menu.getId(), menu.getName().getValue(), price, menu.getMenuGroupId());
    }

    public Long getId() {
        return id;
    }
}
