package kitchenpos.menu.dto.request;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.dto.MenuProductDto;

public class CreateMenuRequest {
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductDto> menuProducts;

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
