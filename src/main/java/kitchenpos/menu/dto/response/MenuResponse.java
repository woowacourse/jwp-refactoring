package kitchenpos.menu.dto.response;

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

    private MenuResponse(final Long id, final String name, final BigDecimal price,
        final Long menuGroupId, final List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
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

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }

    public static MenuResponse of(final Menu menu, final List<MenuProduct> menuProducts) {
        return new MenuResponse(
            menu.getId(),
            menu.getName(),
            menu.getPrice(),
            menu.getMenuGroupId(),
            menuProducts.stream()
                .map(it -> new MenuProductResponse(
                    it.getSeq(),
                    it.getMenuId(),
                    it.getProductId(),
                    it.getQuantity()
                ))
                .collect(Collectors.toList())
        );
    }
}
