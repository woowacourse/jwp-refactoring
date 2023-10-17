package kitchenpos.ui.dto.menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class CreateMenuRequest {

    private final String name;
    private final Integer price;
    private final Long menuGroupId;
    private final List<MenuProductDto> menuProducts;

    public CreateMenuRequest(
            final String name,
            final Integer price,
            final Long menuGroupId,
            final List<MenuProductDto> menuProducts
    ) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu toDomain() {
        return new Menu(name, BigDecimal.valueOf(price), menuGroupId,
                menuProducts.stream()
                        .map(menuProductDto -> new MenuProduct(null, menuProductDto.getProductId(), menuProductDto.getQuantity()))
                        .collect(Collectors.toList())
        );
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

    public List<MenuProductDto> getMenuProducts() {
        return menuProducts;
    }

}
