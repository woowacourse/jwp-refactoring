package kitchenpos.application.dto;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;

public class MenuCreateRequest {
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductCreateRequest> menuProducts;

    private MenuCreateRequest() {
    }

    public MenuCreateRequest(String name, BigDecimal price, Long menuGroupId,
        List<MenuProductCreateRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu toEntity() {
        return new Menu(null, name, price, menuGroupId);
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

    public List<MenuProductCreateRequest> getMenuProducts() {
        return menuProducts;
    }
}
