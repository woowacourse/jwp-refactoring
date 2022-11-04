package kitchenpos.menu.application.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.MenuProductRequest.Create;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class MenuRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<Create> menuProducts;

    private MenuRequest() {
    }

    public MenuRequest(String name, BigDecimal price, Long menuGroupId,
                       List<Create> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu toMenu() {
        List<MenuProduct> menuProducts = this.menuProducts.stream()
                .map(it -> new MenuProduct(it.getProductId(), it.getQuantity()))
                .collect(Collectors.toList());
        return new Menu(name, price, menuGroupId, menuProducts);
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

    public List<Create> getMenuProducts() {
        return menuProducts;
    }
}
