package kitchenpos.ui.dto.response;

import kitchenpos.domain.Menu;

import java.math.BigDecimal;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class MenuResponse {
    
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    private MenuResponse() {
    }

    private MenuResponse(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse create(Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId(), getMenuProductResponses(menu));
    }

    private static List<MenuProductResponse> getMenuProductResponses(Menu menu) {
        return menu.getMenuProducts().stream()
                .map(MenuProductResponse::create)
                .collect(toList());
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