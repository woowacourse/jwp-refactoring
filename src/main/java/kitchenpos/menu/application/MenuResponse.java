package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class MenuResponse {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductResponse> menuProducts;

    public MenuResponse(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPrice();
        this.menuGroupId = menu.getMenuGroupId();
        this.menuProducts = toMenuProductResponse(menu.getMenuProducts());
    }

    private List<MenuProductResponse> toMenuProductResponse(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProductResponse::new)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
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
        return menuProducts.stream()
                .map(menuProductResponse -> new MenuProduct(
                        menuProductResponse.getSeq(),
                        menuProductResponse.getMenuId(),
                        menuProductResponse.getProductId(),
                        menuProductResponse.getQuantity()
                ))
                .collect(Collectors.toList());
    }
}
