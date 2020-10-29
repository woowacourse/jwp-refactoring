package kitchenpos.dto.menu;

import kitchenpos.domain.menu.Menu;

import java.math.BigDecimal;
import java.util.List;

public class MenuResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    private MenuResponse() {
    }

    public MenuResponse(Long id) {
        this.id = id;
    }

    private MenuResponse(Long id, String name, BigDecimal price,
                         Long menuGroupId, List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse of(Menu menu, List<MenuProductResponse> menuProducts) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getPriceValue(),
                menu.getMenuGroupId(),
                menuProducts
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
