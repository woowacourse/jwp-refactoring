package kitchenpos.application.dto.response;

import java.math.BigDecimal;
import java.util.List;

public class MenuResponseDto {

    private Long id;
    private String name;
    private BigDecimal price;
    private List<MenuProductResponseDto> menuProducts;

    private MenuResponseDto() {
    }

    public MenuResponseDto(
        Long id,
        String name,
        BigDecimal price,
        List<MenuProductResponseDto> menuProducts
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
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

    public List<MenuProductResponseDto> getMenuProducts() {
        return menuProducts;
    }
}
