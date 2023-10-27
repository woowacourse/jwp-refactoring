package kitchenpos.menu.dto.menu;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.menu.domain.Menu;

public class CreateMenuRequest {

    private final String name;
    private final Integer price;
    private final Long menuGroupId;
    private final List<MenuProductRequest> menuProducts;

    public CreateMenuRequest(
            final String name,
            final Integer price,
            final Long menuGroupId,
            final List<MenuProductRequest> menuProducts
    ) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu toDomain() {
        return new Menu(name, BigDecimal.valueOf(price), menuGroupId);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return BigDecimal.valueOf(price);
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

}
