package kitchenpos.application.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;

public class MenuResponse {

    private final long id;
    private final String name;
    private final BigDecimal price;
    private final long menuGroupId;
    private final List<MenuProductResponse> menuProducts;

    private MenuResponse(final long id, final String name, final BigDecimal price, final long menuGroupId,
                         final List<MenuProductResponse> menuProducts) {
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
                menu.getPrice().value(),
                menu.getMenuGroup().getId(),
                MenuProductResponse.from(menu.getMenuProducts())
        );
    }

    public static List<MenuResponse> from(final List<Menu> menus) {
        return menus.stream()
                .map(MenuResponse::from).
                collect(Collectors.toList());
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
