package kitchenpos.menu.ui.request;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.ui.request.MenuProductCreateRequest;

public class MenuCreateRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductCreateRequest> menuProducts;

    private MenuCreateRequest() {
    }

    public MenuCreateRequest(final String name, final BigDecimal price, final Long menuGroupId,
                             final List<MenuProductCreateRequest> menuProducts) {
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

    public List<MenuProductCreateRequest> getMenuProducts() {
        return menuProducts;
    }

    public Menu toEntity() {
        return new Menu(name, price, menuGroupId, this.menuProducts
                .stream()
                .map(MenuProductCreateRequest::toEntity)
                .collect(Collectors.toList()));
    }
}
