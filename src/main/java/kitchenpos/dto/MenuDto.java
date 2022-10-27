package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProductDto;

public class MenuDto {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductDto> menuProductDtos;

    public MenuDto(String name, BigDecimal price, Long menuGroupId, List<MenuProductDto> menuProductDtos) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductDtos = menuProductDtos;
    }

    public MenuDto(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPrice();
        this.menuGroupId = menu.getMenuGroup().getId();
        this.menuProductDtos = menu.getProducts()
                .stream()
                .map(MenuProductDto::new)
                .collect(Collectors.toUnmodifiableList());
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
        return menuProductDtos;
    }
}
