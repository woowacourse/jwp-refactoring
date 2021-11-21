package kitchenpos.application.dto.request;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;

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
