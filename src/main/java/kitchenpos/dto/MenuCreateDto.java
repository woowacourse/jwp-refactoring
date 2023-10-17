package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuCreateDto {

    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductCreateDto> menuProducts;

    public MenuCreateDto(final String name, final BigDecimal price, final Long menuGroupId,
                         final List<MenuProductCreateDto> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu toDomain() {
        final List<MenuProduct> menuProductDomains = menuProducts.stream()
                .map(MenuProductCreateDto::toDomain)
                .collect(Collectors.toList());
        return new Menu(name, price, menuGroupId, menuProductDomains);
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

    public List<MenuProductCreateDto> getMenuProducts() {
        return menuProducts;
    }
}
