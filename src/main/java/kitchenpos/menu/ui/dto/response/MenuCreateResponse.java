package kitchenpos.menu.ui.dto.response;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;

public class MenuCreateResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductCreateResponse> menuProducts;

    public MenuCreateResponse() {
    }

    public MenuCreateResponse(final Long id, final String name, final BigDecimal price, final Long menuGroupId,
                              final List<MenuProductCreateResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuCreateResponse from(final Menu menu) {
        return new MenuCreateResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId(),
                MenuProductCreateResponse.from(menu.getMenuProducts()));
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

    public List<MenuProductCreateResponse> getMenuProducts() {
        return menuProducts;
    }
}
