package kitchenpos.ui.dto.menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;

public class MenuRequest {

    @NotBlank
    private String name;

    @DecimalMin("0")
    private BigDecimal price;

    @NotNull
    private Long menuGroupId;

    @NotEmpty
    private List<MenuProductRequest> menuProducts;

    protected MenuRequest() {
    }

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

    public Menu toEntity(BigDecimal sumOfProductsPrice, MenuGroup menuGroup) {
        if (Objects.isNull(price) || price.compareTo(sumOfProductsPrice) > 0) {
            throw new IllegalArgumentException();
        }
        return new Menu(name, price, menuGroup);
    }
}
