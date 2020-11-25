package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;

public class MenuCreateRequest {

    private String name;
    private Long price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProductRequest;

    public MenuCreateRequest() {
    }

    public MenuCreateRequest(String name, Long price, Long menuGroupId,
        List<MenuProductRequest> menuProductRequest) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductRequest = menuProductRequest;
    }

    public List<MenuProductRequest> getMenuProductRequest() {
        return menuProductRequest;
    }

    public Long getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public String getName() {
        return name;
    }

    public Menu toEntity(MenuGroup menuGroup) {
        return new Menu(name, BigDecimal.valueOf(price), menuGroup);
    }
}
