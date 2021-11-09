package kitchenpos.ui.dto.response;

import kitchenpos.domain.Menu;

import java.math.BigDecimal;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class MenuResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private MenuGroupResponse menuGroup;
    private List<MenuProductResponse> menuProducts;

    private MenuResponse(Long id, String name, BigDecimal price, MenuGroupResponse menuGroup, List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse create(Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), MenuGroupResponse.create(menu.getMenuGroup()), getMenuProductResponses(menu));
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

    public MenuGroupResponse getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}