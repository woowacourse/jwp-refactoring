package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;

public class MenuDto {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
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
        return new MenuDto(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId(), menuProductDtos);
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
