package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class CreateMenuDto {

    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<CreateMenuProductDto> menuProducts;

    public CreateMenuDto(final String name, final BigDecimal price, final Long menuGroupId, final List<CreateMenuProductDto> menuProducts) {
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

    public List<MenuProduct> toMenuProducts() {
        return menuProducts.stream()
                           .map(CreateMenuProductDto::toMenuProduct)
                           .collect(Collectors.toList());
    }
}
