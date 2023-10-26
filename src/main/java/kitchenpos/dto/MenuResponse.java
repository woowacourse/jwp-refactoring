package kitchenpos.dto;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menuGroup.MenuGroup;

import java.math.BigDecimal;

public class MenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private MenuGroupResponse menuGroup;

    public MenuResponse(Long id, String name, BigDecimal price, MenuGroupResponse menuGroup) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    public static MenuResponse toResponse(Menu menu, MenuGroup menuGroup) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), MenuGroupResponse.toResponse(menuGroup));
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

    public MenuGroupResponse getMenuGroup() {
        return menuGroup;
    }
}
