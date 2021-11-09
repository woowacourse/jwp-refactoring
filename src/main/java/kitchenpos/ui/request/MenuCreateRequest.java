package kitchenpos.ui.request;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;

public class MenuCreateRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public static MenuCreateRequest create(String name, BigDecimal price, Long menuGroupId,
                             List<MenuProductRequest> menuProducts) {
        final MenuCreateRequest menuCreateRequest = new MenuCreateRequest();
        menuCreateRequest.name = name;
        menuCreateRequest.price = price;
        menuCreateRequest.menuGroupId = menuGroupId;
        menuCreateRequest.menuProducts = menuProducts;
        return menuCreateRequest;
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

    public Menu toEntity() {
        final List<MenuProduct> menuProducts = this.menuProducts.stream()
            .map(MenuProductRequest::toEntity)
            .collect(Collectors.toList());

        return Menu.builder()
            .name(name)
            .price(price)
            .menuGroup(MenuGroup.create(menuGroupId))
            .menuProducts(menuProducts)
            .build();
    }
}
