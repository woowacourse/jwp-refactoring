package kitchenpos.application.dto.menu;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuCreateResponse {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductCreateResponse> menuProducts;

    public MenuCreateResponse(final Long id, final String name, final BigDecimal price, final Long menuGroupId,
                              final List<MenuProductCreateResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuCreateResponse of(final Menu menu) {
        final List<MenuProduct> menuProducts = menu.getMenuProducts();
        final List<MenuProductCreateResponse> menuProductCreateResponses = menuProducts.stream()
                .map(MenuProductCreateResponse::of)
                .collect(toList());
        return new MenuCreateResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getMenuGroupId(),
                menuProductCreateResponses
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

    public List<MenuProductCreateResponse> getMenuProducts() {
        return menuProducts;
    }
}
