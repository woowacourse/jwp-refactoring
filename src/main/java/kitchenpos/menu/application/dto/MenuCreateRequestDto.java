package kitchenpos.menu.application.dto;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.menu.model.Menu;
import kitchenpos.menuproduct.application.dto.MenuProductCreateRequestDto;

public class MenuCreateRequestDto {
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductCreateRequestDto> menuProductCreateRequestDto;

    private MenuCreateRequestDto() {
    }

    public MenuCreateRequestDto(String name, BigDecimal price, Long menuGroupId,
        List<MenuProductCreateRequestDto> menuProductCreateRequestDto) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductCreateRequestDto = menuProductCreateRequestDto;
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

    public List<MenuProductCreateRequestDto> getMenuProductRequestDto() {
        return menuProductCreateRequestDto;
    }

    public Menu toEntity() {
        return new Menu(null, name, price, menuGroupId);
    }
}
