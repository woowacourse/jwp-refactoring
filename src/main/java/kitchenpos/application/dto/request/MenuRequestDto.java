package kitchenpos.application.dto.request;

import java.math.BigDecimal;
import java.util.List;

public class MenuRequestDto {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequestDto> menuProducts;

    private MenuRequestDto() {
    }

    public MenuRequestDto(
        String name,
        BigDecimal price,
        Long menuGroupId,
        List<MenuProductRequestDto> menuProducts
    ) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
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

    public List<MenuProductRequestDto> getMenuProducts() {
        return menuProducts;
    }
}
