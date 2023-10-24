package kitchenpos.ui.menu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.menu.dto.CreateMenuCommand;
import kitchenpos.application.menu.dto.MenuProductCommand;

public class CreateMenuRequest {

    @JsonProperty("name")
    private String name;
    @JsonProperty("price")
    private BigDecimal price;
    @JsonProperty("menuGroupId")
    private Long menuGroupId;
    @JsonProperty("menuProducts")
    private List<MenuProductRequest> menuProducts;

    public CreateMenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public CreateMenuCommand toCommand() {
        List<MenuProductCommand> menuProductCommands = menuProducts.stream()
                .map(it -> new MenuProductCommand(it.productId(), it.quantity()))
                .collect(Collectors.toList());
        return new CreateMenuCommand(name, price, menuGroupId, menuProductCommands);
    }

    public String name() {
        return name;
    }

    public BigDecimal price() {
        return price;
    }

    public Long menuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> menuProducts() {
        return menuProducts;
    }
}
