package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.List;

public class Menu {
    private Long id;
    private String name;
    private MenuPrice price;
    private Long menuGroupId;
    private MenuProducts menuProducts;

    public Menu() {
    }

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = new MenuPrice(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts = new MenuProducts(menuProducts);
        validateMenuProductsPrice(this.menuProducts);
    }

    private void validateMenuProductsPrice(MenuProducts menuProducts) {
        BigDecimal totalPrice = menuProducts.calculateTotalPrice();
        if (price.isBigger(totalPrice)) {
            throw new IllegalArgumentException();
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

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public void setPrice(final BigDecimal price) {
        this.price = new MenuPrice(price);
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public void setMenuGroupId(final Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getCollection();
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = new MenuProducts(menuProducts);
    }
}
