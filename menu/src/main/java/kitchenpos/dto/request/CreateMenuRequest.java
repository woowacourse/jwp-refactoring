package kitchenpos.dto.request;

import com.sun.istack.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class CreateMenuRequest {

    @NotNull
    private String name;
    @NotNull
    private BigDecimal price;
    @NotNull
    private Long menuGroupId;
    @NotNull
    private List<MenuProductRequest> menuProducts;

    public CreateMenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
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

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }
}
