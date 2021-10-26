package kitchenpos.ui.dto;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class MenuRequest {
    @NotBlank
    private String name;
    @NotBlank
    private BigDecimal price;
    @NotBlank
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    public MenuRequest() {}

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public Menu toMenu() {
        return new Menu(name, price, menuGroupId, menuProducts);
    }
}
