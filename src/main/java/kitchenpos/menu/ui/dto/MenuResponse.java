package kitchenpos.menu.ui.dto;

import kitchenpos.menu.domain.Menu;

import java.util.List;
import java.util.stream.Collectors;

public class MenuResponse {

    private long id;
    private String name;
    private Long price;
    private long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    private MenuResponse() {
    }

    private MenuResponse(final long id, final String name, final Long price, final long menuGroupId, final List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse from(final Menu menu) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice().getPrice(),
                menu.getMenuGroupId(),
                menu.getMenuProducts()
                        .stream()
                        .map(it -> MenuProductResponse.of(it, menu.getId()))
                        .collect(Collectors.toList())
        );
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}
