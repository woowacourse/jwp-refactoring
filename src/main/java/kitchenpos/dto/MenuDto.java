package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;

public class MenuDto {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductDto> menuProducts;

    public MenuDto(final Long id, final String name, final BigDecimal price, final Long menuGroupId,
                   final List<MenuProductDto> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuDto toDto(final Menu menu) {
        final List<MenuProductDto> menuProductDtos = menu.getMenuProducts()
                .stream().map(MenuProductDto::toDto)
                .collect(Collectors.toList());
        return new MenuDto(menu.getId(), menu.getName(), menu.getPrice().getValue(), menu.getMenuGroup().getId(), menuProductDtos);
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

    public List<MenuProductDto> getMenuProducts() {
        return menuProducts;
    }
}
