package kitchenpos.dto.menu.request;

import java.math.BigDecimal;
import java.util.List;

public class MenuCreateRequest {
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductCreateRequest> menuProducts;

    public MenuCreateRequest(
            final String name,
            final BigDecimal price,
            final Long menuGroupId,
            final List<MenuProductCreateRequest> menuProducts
    ) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public String name() {
        return name;
    }

    public BigDecimal price() {
        return price;
    }

    public Long menuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductCreateRequest> menuProducts() {
        return menuProducts;
    }
}
