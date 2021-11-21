package kitchenpos.application.dto.request.menu;

import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;

public class MenuRequest {

    private String name;
    private int price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProductRequests;

    public MenuRequest() {
    }

    public MenuRequest(String name, int price, Long menuGroupId,
            List<MenuProductRequest> menuProductRequests) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductRequests = menuProductRequests;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProductRequests() {
        return menuProductRequests;
    }

    public Menu toEntity(MenuGroup menuGroup, List<MenuProduct> menuProducts) {

        return new Menu.MenuBuilder()
                .setName(this.name)
                .setPrice(this.price)
                .setMenuGroup(menuGroup)
                .setMenuProducts(menuProducts)
                .build();
    }
}
