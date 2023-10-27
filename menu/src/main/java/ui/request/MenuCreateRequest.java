package ui.request;

import java.math.BigDecimal;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class MenuCreateRequest {

    @NotBlank
    private final String name;

    @NotNull
    private final BigDecimal price;

    @NotNull
    private final Long menuGroupId;
    private final List<MenuProductCreateRequest> menuProducts;

    public MenuCreateRequest(
            final String name,
            final BigDecimal price,
            final Long menuGroupId,
            final List<MenuProductCreateRequest> menuProducts
    ) {
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

    public List<MenuProductCreateRequest> getMenuProducts() {
        return menuProducts;
    }
}
