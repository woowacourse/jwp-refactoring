package kitchenpos.ui.dto.request;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.ui.dto.MenuProductDto;

public class MenuCreateRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductDto> menuProducts;

    private MenuCreateRequest() {
    }

    public MenuCreateRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductDto> menuProducts) {
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

    public List<MenuProductDto> getMenuProducts() {
        return menuProducts;
    }
}
