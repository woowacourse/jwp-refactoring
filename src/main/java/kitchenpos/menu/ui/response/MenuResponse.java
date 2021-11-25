package kitchenpos.menu.ui.response;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;

public class MenuResponse {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductResponse> menuProducts;

    private MenuResponse(
        Long id,
        String name,
        BigDecimal price,
        Long menuGroupId,
        List<MenuProductResponse> menuProducts
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse from(Menu menu) {
        return new MenuResponse(
            menu.getId(),
            menu.getName(),
            menu.getPrice(),
            menu.getMenuGroupId(),
            MenuProductResponse.of(menu.getMenuProducts())
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}
