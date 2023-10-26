package kitchenpos.dto.request;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Price;

public class MenuCreateRequest {

    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductCreateRequest> menuProducts;

    public MenuCreateRequest(String name, BigDecimal price, Long menuGroupId,
                             List<MenuProductCreateRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu toEntity() {
        return new Menu(null, name, new Price(price), menuGroupId, new ArrayList<>());
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
