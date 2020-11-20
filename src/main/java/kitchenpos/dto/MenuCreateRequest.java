package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;

public class MenuCreateRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProductRequest;

    public MenuCreateRequest() {
    }

    private MenuCreateRequest(String name, BigDecimal price, Long menuGroupId,
        List<MenuProductRequest> menuProductRequest) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductRequest = menuProductRequest;
    }

    public static MenuCreateRequest of(Menu menu) {
        String name = menu.getName();
        BigDecimal price = menu.getPrice();
        MenuGroup menuGroup = menu.getMenuGroup();
        List<MenuProduct> menuProducts = menu.getMenuProducts();

        return new MenuCreateRequest(name, price, menuGroup.getId(),
            MenuProductRequest.toRequestList(menuProducts));
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

    public List<MenuProductRequest> getMenuProductRequests() {
        return menuProductRequest;
    }

    public Menu toEntity(MenuGroup menuGroup) {
        return new Menu(name, price, menuGroup);
    }
}