package menu.main.java.kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.dto.MenuProductRequest;

public class MenuRequest {
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductRequest> menuProducts;

    public MenuRequest(
            final String name,
            final BigDecimal price,
            final Long menuGroupId,
            final List<MenuProductRequest> menuProducts
    ) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
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

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }
}
