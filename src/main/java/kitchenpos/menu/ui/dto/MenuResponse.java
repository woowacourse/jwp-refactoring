package kitchenpos.menu.ui.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MenuResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    private MenuResponse() {
    }

    private MenuResponse(Long id,
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

    public static MenuResponse of(Menu menu, List<MenuProduct> menuProducts) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getMenuGroupId(),
                MenuProductResponse.from(menuProducts)
        );
    }

    public static List<MenuResponse> from(Map<Menu, List<MenuProduct>> results) {
        List<MenuResponse> menuResponses = new ArrayList<>();
        results.forEach((key, value) -> menuResponses.add(
                MenuResponse.of(key, value)
        ));
        return menuResponses;
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
