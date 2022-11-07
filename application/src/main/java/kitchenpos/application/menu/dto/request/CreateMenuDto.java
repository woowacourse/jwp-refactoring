package kitchenpos.application.menu.dto.request;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.core.domain.menu.Menu;
import kitchenpos.core.domain.menu.ProductQuantities;

public class CreateMenuDto {

    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<CreateMenuProductDto> menuProducts;

    public CreateMenuDto(String name,
                         BigDecimal price,
                         Long menuGroupId,
                         List<CreateMenuProductDto> menuProducts) {
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

    public List<CreateMenuProductDto> getMenuProducts() {
        return menuProducts;
    }

    public Menu toEntity(ProductQuantities productQuantities) {
        return Menu.of(name, price, menuGroupId, productQuantities);
    }
}
