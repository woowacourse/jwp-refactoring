package kitchenpos.application.command;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import kitchenpos.domain.model.menu.Menu;
import kitchenpos.domain.model.menu.MenuProduct;

public class CreateMenuCommand {
    @NotBlank
    private String name;
    @Min(0)
    private BigDecimal price;
    @NotNull
    private Long menuGroupId;
    @NotEmpty
    private List<MenuProduct> menuProducts;

    private CreateMenuCommand() {
    }

    public CreateMenuCommand(String name, BigDecimal price, Long menuGroupId,
            List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu toEntity() {
        return new Menu(null, name, price, menuGroupId, menuProducts);
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

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
