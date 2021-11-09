package kitchenpos.application.dtos;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;

public class MenuResponse {
    private final Long id;
    private final String name;
    private final Long price;
    private final Long MenuGroupId;
    private final List<MenuProductResponse> menuProducts;

    public MenuResponse(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPrice().longValue();
        this.MenuGroupId = menu.getMenuGroupId();
        this.menuProducts = menu.getMenuProducts().stream()
                .map(MenuProductResponse::new)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return MenuGroupId;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}
