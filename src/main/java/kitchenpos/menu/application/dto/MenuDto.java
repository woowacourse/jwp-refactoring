package kitchenpos.menu.application.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.product.application.dto.MenuProductDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuDto {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductDto> menuProducts;

    private MenuDto(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProductDto> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuDto from(Menu menu) {
        Long id = menu.getId();
        String name = menu.getName();
        BigDecimal price = menu.getPrice();
        Long menuGroupId = menu.getMenuGroupId();
        List<MenuProductDto> menuProducts = menu.getMenuProducts().stream()
                .map(MenuProductDto::from)
                .collect(Collectors.toList());
        return new MenuDto(id, name, price, menuGroupId, menuProducts);
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
