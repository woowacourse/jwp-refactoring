package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Menu {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private MenuProducts menuProducts;

    public Menu(final Long id, final String name, final BigDecimal price, final Long menuGroupId,
                final MenuProducts menuProducts) {
        validatePrice(price);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu(final String name, final BigDecimal price, final Long menuGroupId) {
        this(null, name, price, menuGroupId, new MenuProducts(new ArrayList<>()));
    }

    private void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public void addMenuIdToMenuProducts() {
        menuProducts.addMenuId(id);
    }

    public void addMenuProduct(final MenuProduct... menuProducts) {
        this.menuProducts.addMenuProduct(menuProducts);
    }

    public void changeAllMenuProducts(final MenuProducts menuProducts) {
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

    public List<MenuProduct> getAllMenuProduct() {
        return menuProducts.getMenuProducts();
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }
}
