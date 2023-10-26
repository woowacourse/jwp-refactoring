package kitchenpos.menu.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.dto.response.MenuGroupResponse;

public class MenuResponse {
    private final long id;
    private final String name;
    private final BigDecimal price;
    private final MenuGroupResponse menuGroup;
    private final List<MenuProductResponse> menuProducts;

    private MenuResponse(
            final long id,
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

    public static MenuResponse of(final Menu menu, final List<MenuProduct> menuProducts) {
        return new MenuResponse(
                menu.id(),
                menu.name(),
                menu.price().price(),
                MenuGroupResponse.from(menu.menuGroup()),
                menuProducts.stream()
                        .map(MenuProductResponse::from)
                        .collect(Collectors.toUnmodifiableList())
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

    public MenuGroupResponse getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}
