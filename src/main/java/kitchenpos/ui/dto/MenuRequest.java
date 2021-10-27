package kitchenpos.ui.dto;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuRequest {
    @NotBlank
    private String name;
    @NotBlank
    private BigDecimal price;
    @NotBlank
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public MenuRequest() {
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

    public Menu toMenu() {
        List<MenuProduct> menuProducts = this.menuProducts.stream()
                .map(MenuProductRequest::toMenuProduct)
                .collect(Collectors.toList());

        return new Menu(
                name,
                price,
                menuGroupId,
                menuProducts
        );
    }
}
