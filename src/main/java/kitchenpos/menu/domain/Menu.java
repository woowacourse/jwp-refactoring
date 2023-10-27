package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;

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

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }
}
