package kitchenpos.dto;

import kitchenpos.domain.Menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuResponse {

    private long id;
    private String name;
    private BigDecimal price;
    private MenuGroupResponse menuGroup;
    private List<MenuProductResponse> menuProducts;

    public MenuResponse(final long id,
                        final String name,
                        final BigDecimal price,
                        final MenuGroupResponse menuGroup,
                        final List<MenuProductResponse> menuProducts
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse from(final Menu menu) {
        return new MenuResponse(
                menu.getId(),
                menu.getName().getValue(),
                menu.getPrice().getValue(),
                MenuGroupResponse.from(menu.getMenuGroup()),
                convertToMenuProducts(menu)
        );
    }

    private static List<MenuProductResponse> convertToMenuProducts(final Menu menu) {
        return menu.getMenuProducts()
                .getMenuProductItems()
                .stream()
                .map(MenuProductResponse::from)
                .collect(Collectors.toList());
    }

    public static List<MenuResponse> from(final List<Menu> menus) {
        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
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

    public MenuGroupResponse getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}
