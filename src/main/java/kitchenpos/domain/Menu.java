package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;

public class Menu {
    private Long id;
    private String name;
    private Long price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    public Menu(Long id, String name, Long price, Long menuGroupId, List<MenuProduct> menuProducts) {
        validateName(name);
        validatePrice(price);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    private void validatePrice(Long price) {
        if (price == null || price < 0L) {
            throw new IllegalArgumentException();
        }
    }

    private void validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException();
        }
    }

    public Menu(String name, Long price, Long menuGroupId) {
        this(null, name, price, menuGroupId, null);
    }

    public Menu changeMenuProducts(List<MenuProduct> menuProducts) {
        return new Menu(this.id, this.name, this.price, this.menuGroupId, menuProducts);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
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
