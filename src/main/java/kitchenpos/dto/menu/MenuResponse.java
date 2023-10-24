package kitchenpos.dto.menu;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;

public class MenuResponse {

    private Long id;
    private String name;
    private Integer price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    public MenuResponse(final Long id,
                        final String name,
                        final Integer price,
                        final Long menuGroupId,
                        final List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse from(final Menu menu) {
        final List<MenuProduct> menuProducts = menu.getMenuProducts();
        final List<MenuProductResponse> menuProductResponses = menuProducts.stream()
            .map(MenuProductResponse::from)
            .collect(Collectors.toUnmodifiableList());

        return new MenuResponse(menu.getId(),
            menu.getName(),
            menu.getPrice().intValue(),
            menu.getMenuGroupId(),
            menuProductResponses);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}
