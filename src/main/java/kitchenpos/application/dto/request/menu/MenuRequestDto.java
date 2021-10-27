package kitchenpos.application.dto.request.menu;

import java.math.BigDecimal;
import java.util.List;

public class MenuRequestDto {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequestDto> menuProductRequestDtos;

    private MenuRequestDto() {
    }

    public MenuRequestDto(
        String name,
        BigDecimal price,
        Long menuGroupId,
        List<MenuProductRequestDto> menuProductRequestDtos
    ) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductRequestDtos = menuProductRequestDtos;
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

    public List<MenuProductRequestDto> getMenuProductRequestDtos() {
        return menuProductRequestDtos;
    }
}
