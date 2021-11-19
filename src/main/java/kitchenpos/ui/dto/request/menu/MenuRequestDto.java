package kitchenpos.ui.dto.request.menu;

import java.math.BigDecimal;
import java.util.List;

public class MenuRequestDto {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequestDto> menuProducts;

    private MenuRequestDto() {
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
