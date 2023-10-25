package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;

public class Menu {
    private Long id;
    private String name;
    private Price price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    public Menu(String name, Price price, Long menuGroupId) {
        this(null, name, price, menuGroupId, null);
    }

    public Menu(Long id, String name, Price price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public void assignProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
        for (MenuProduct menuProduct : this.menuProducts) {
            menuProduct.setMenuId(this.id);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = new Price(price);
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public void setMenuGroupId(final Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }
}
