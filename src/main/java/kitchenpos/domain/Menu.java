package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;

public class Menu {
    private final Long id;
    private final String name;
    private final Price price;
    private final Long menuGroupId;
    private List<MenuProduct> menuProducts;

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId,
                List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = new Price(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public void updateMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
