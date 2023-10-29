package kitchenpos.menu.application.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import java.util.List;

public class MenuCreateRequest {

    private final String name;
    private final BigDecimal price;
    private final long menuGroupId;
    private final List<MenuProductRequest> menuProducts;

    @JsonCreator
    public MenuCreateRequest(final String name, final BigDecimal price, final long menuGroupId,
                             final List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }
}
