package kitchenpos.dto.menu;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menugroup.MenuGroup;

public class MenuRequest {

    @NotBlank
    private String name;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;

    @NotNull
    private Long menuGroupId;

    @NotEmpty
    private List<ProductQuantityRequest> productQuantities;

    protected MenuRequest() {
    }

    public MenuRequest(final String name, final BigDecimal price, final Long menuGroupId,
            final List<ProductQuantityRequest> productQuantities) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.productQuantities = productQuantities;
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

    public List<ProductQuantityRequest> getProductQuantities() {
        return productQuantities;
    }

    public Menu toEntity(final MenuGroup menuGroup) {
        return new Menu(this.name, this.price, menuGroup);
    }
}
