package kitchenpos.menu.dto;

import java.math.BigDecimal;

import kitchenpos.menu.domain.Menu;

public class MenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private MenuGroupResponse menuGroupResponse;

    private MenuResponse(Long id, String name, BigDecimal price, MenuGroupResponse menuGroupResponse) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupResponse = menuGroupResponse;
    }

    public static MenuResponse from(Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(),
            MenuGroupResponse.from(menu.getMenuGroup()));
    }

    public MenuGroupResponse getMenuGroupResponse() {
        return menuGroupResponse;
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

}
