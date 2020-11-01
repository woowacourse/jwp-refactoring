package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import kitchenpos.menu.domain.Menu;

public class MenuCreateRequest {

    private String name;
    private Long price;
    private Long menuGroupId;
    private List<MenuProductDto> menuProductDtos;

    public MenuCreateRequest() {
    }

    public MenuCreateRequest(String name, Long price, Long menuGroupId,
        List<MenuProductDto> menuProductDtos) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductDtos = menuProductDtos;
    }

    public List<MenuProductDto> getMenuProductDtos() {
        return menuProductDtos;
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

    public Menu toEntity() {
        return new Menu(name, BigDecimal.valueOf(price), menuGroupId, new ArrayList<>());
    }
}
