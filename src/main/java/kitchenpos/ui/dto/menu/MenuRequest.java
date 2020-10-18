package kitchenpos.ui.dto.menu;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

public class MenuRequest {

    @NotBlank
    private String name;

    @DecimalMin("0")
    private BigDecimal price;

    private Long menuGroupId;

    private List<MenuProductRequest> menuProducts;

    protected MenuRequest() { }

    public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
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

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    public Menu toEntity(BigDecimal sum, MenuGroup menuGroup) {
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
        return new Menu(name, price, menuGroup);
    }
}
