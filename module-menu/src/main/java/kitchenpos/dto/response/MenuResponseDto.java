package kitchenpos.dto.response;

import java.math.BigDecimal;
import java.util.List;

public class MenuResponseDto {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductResponseDto> menuProducts;

    private MenuResponseDto() {
    }

    public MenuResponseDto(
        Long id,
        String name,
        BigDecimal price,
        Long menuGroupId,
        List<MenuProductResponseDto> menuProducts
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
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

    public List<MenuProductResponseDto> getMenuProducts() {
        return menuProducts;
    }
}
