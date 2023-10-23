package kitchenpos.menu.application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import java.util.List;

public class MenuCreationRequest {

    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductWithQuantityRequest> menuProducts;

    @JsonCreator
    public MenuCreationRequest(
            final String name,
            final BigDecimal price,
            final Long menuGroupId,
            final List<MenuProductWithQuantityRequest> menuProducts
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

    public List<MenuProductWithQuantityRequest> getMenuProducts() {
        return menuProducts;
    }
}
