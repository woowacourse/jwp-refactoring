package kitchenpos.application.dto;

import java.math.BigDecimal;
import java.util.List;

public class MenuCreateDto {

    private final String name;
    private final Long menuGroupId;
    private final BigDecimal price;
    private final List<MenuProductCreateDto> menuProductCreateDtos;

    public MenuCreateDto(final String name, final Long menuGroupId, final BigDecimal price,
        final List<MenuProductCreateDto> menuProductCreateDtos) {
        this.name = name;
        this.menuGroupId = menuGroupId;
        this.price = price;
        this.menuProductCreateDtos = menuProductCreateDtos;
    }

    public String getName() {
        return name;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public List<MenuProductCreateDto> getMenuProductCreateDtos() {
        return menuProductCreateDtos;
    }
}
