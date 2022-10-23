package kitchenpos.application.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuDto {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductDto> menuProducts;

    public MenuDto(Long id,
                   String name,
                   BigDecimal price,
                   Long menuGroupId,
                   List<MenuProductDto> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuDto of(Menu menu, List<MenuProduct> menuProducts) {
        List<MenuProductDto> menuProductDtos = menuProducts.stream()
                .map(MenuProductDto::of)
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
