package kitchenpos.ui.menu.dto;

import kitchenpos.domain.Menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuResponse {

    private long id;
    private String name;
    private BigDecimal price;
    private long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    private MenuResponse() {
    }

    private MenuResponse(final long id, final String name, final BigDecimal price, final long menuGroupId, final List<MenuProductResponse> menuProducts) {
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
                menu.getPrice(),
                menu.getMenuGroupId(),
                menu.getMenuProducts()
                        .stream()
                        .map(MenuProductResponse::from)
                        .collect(Collectors.toList())
        );
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}
