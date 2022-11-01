package kitchenpos.dto.request;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;

public class MenuRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public MenuRequest(final String name, final BigDecimal price, final Long menuGroupId,
                       final List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu toEntity() {
        final Menu menu = Menu.ofNew(name, price, menuGroupId);

        final List<MenuProduct> menuProductEntities = menuProducts.stream()
                .map(menuProductRequest -> menuProductRequest.toEntity(menu))
                .collect(Collectors.toList());
        menu.addMenuProducts(menuProductEntities);

        return menu;
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

    private MenuRequest() {
    }
}
