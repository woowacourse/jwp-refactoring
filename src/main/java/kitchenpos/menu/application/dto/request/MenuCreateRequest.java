package kitchenpos.menu.application.dto.request;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MenuCreateRequest {

    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductCreateRequest> menuProducts;

    public MenuCreateRequest(final String name, final BigDecimal price, final Long menuGroupId,
                             final List<MenuProductCreateRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public MenuCreateRequest(final String name, final BigDecimal price, final Long menuGroupId) {
        this(name, price, menuGroupId, new ArrayList<>());
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.stream()
                .map(MenuProductCreateRequest::toEntity)
                .collect(Collectors.toList());
    }

    public Menu toEntity() {
        return new Menu(name, price, menuGroupId, getMenuProducts());
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
}
