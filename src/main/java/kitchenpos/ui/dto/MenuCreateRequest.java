package kitchenpos.ui.dto;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MenuCreateRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts = new ArrayList<>();

    protected MenuCreateRequest() {
    }

    public MenuCreateRequest(final String name, final BigDecimal price, final Long menuGroupId,
                             final List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu toEntity() {
        final List<MenuProduct> entities = menuProducts.stream()
                .map(MenuProductRequest::toEntity)
                .collect(Collectors.toList());
        return new Menu(name, price, menuGroupId, entities);
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
}
