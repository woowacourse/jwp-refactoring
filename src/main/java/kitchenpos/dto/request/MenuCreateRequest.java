package kitchenpos.dto.request;

import com.sun.istack.NotNull;
import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;

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
        return new Menu(this.name, this.price, this.menuGroupId, menuProducts);
    }
}
