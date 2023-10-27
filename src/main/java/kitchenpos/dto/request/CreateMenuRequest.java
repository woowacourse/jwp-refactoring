package kitchenpos.dto.request;

import java.math.BigDecimal;
import java.util.List;

public class CreateMenuRequest {

    private Long menuGroupId;

    private List<MenuProductDto> menuProducts;

    private String name;

    private BigDecimal price;

    public CreateMenuRequest() {
    }

    public CreateMenuRequest(final Long menuGroupId, final List<MenuProductDto> menuProducts, final String name, final BigDecimal price) {
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
        this.name = name;
        this.price = price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductDto> getMenuProducts() {
        return menuProducts;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
