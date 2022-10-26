package kitchenpos.menu.ui.dto;

import com.sun.istack.NotNull;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Name;
import kitchenpos.menu.domain.Price;

public class MenuCreateRequest {

    @NotNull
    private String name;

    @NotNull
    private Integer price;

    @NotNull
    private Long menuGroupId;

    @NotNull
    private List<MenuProductCreateRequest> menuProducts;

    public MenuCreateRequest() {
    }

    public MenuCreateRequest(final String name, final Integer price, final Long menuGroupId,
                             final List<MenuProductCreateRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
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

    public List<MenuProductCreateRequest> getMenuProducts() {
        return menuProducts;
    }

    public Menu toMenu(final List<MenuProduct> menuProducts) {
        return new Menu(new Name(this.name), Price.valueOf(this.price), this.menuGroupId, menuProducts);
    }
}
