package kitchenpos.ui.dto.response;

import java.math.BigDecimal;
import java.util.List;

public class MenuCreateResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductCreateResponse> menuProducts;

    private MenuCreateResponse() {
    }

    public MenuCreateResponse(final Long id, final String name, final BigDecimal price, final Long menuGroupId,
                              final List<MenuProductCreateResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
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

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductCreateResponse> getMenuProducts() {
        return menuProducts;
    }
}
