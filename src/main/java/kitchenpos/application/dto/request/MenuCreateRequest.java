package kitchenpos.application.dto.request;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuCreateRequest {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    public MenuCreateRequest() {
    }

    public MenuCreateRequest(final Long id, final String name, final BigDecimal price, final Long menuGroupId,
                             final List<MenuProduct> menuProducts) {
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

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public Menu toMenu() {
        return new Menu(
                this.id,
                this.name,
                this.price,
                this.menuGroupId,
                this.menuProducts);
    }
}
