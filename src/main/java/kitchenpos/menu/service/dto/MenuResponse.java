package kitchenpos.menu.service.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.Price;
import kitchenpos.menu.domain.Menu;

public class MenuResponse {

    private Long id;
    private String name;
    private Price price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    public MenuResponse(Long id, String name, Price price, Long menuGroupId,
        List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse of(Menu menu) {
        return new MenuResponse(
            menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId(),
            menu.getMenuProducts().stream()
                .map(MenuProductResponse::of)
                .collect(Collectors.toList())
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}
