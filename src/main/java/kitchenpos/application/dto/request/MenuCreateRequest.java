package kitchenpos.application.dto.request;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuCreateRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductDto> menuProducts;

    public MenuCreateRequest() {
    }

    public MenuCreateRequest(final String name,
                             final BigDecimal price,
                             final Long menuGroupId,
                             final List<MenuProductDto> menuProducts) {
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

    public Menu toMenu() {
        return new Menu(
                this.name,
                this.price,
                this.menuGroupId,
                toMenuProducts());
    }

    private List<MenuProduct> toMenuProducts() {
        return this.menuProducts.stream()
                .map(MenuProductDto::toMenuProduct)
                .collect(Collectors.toList());
    }
}
