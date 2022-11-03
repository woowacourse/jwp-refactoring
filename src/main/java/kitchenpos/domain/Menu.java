package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;

public class Menu {
    private final Long id;
    private final String name;
    private final Price price;
    private final Long menuGroupId;
    private MenuProducts menuProducts;

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId, MenuProducts menuProducts) {
        this.id = id;
        this.name = name;
        this.price = new Price(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu(String name, BigDecimal price, Long menuGroupId, MenuProducts menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    public Menu(long id, String name, BigDecimal price, long menuGroupId) {
        this(id, name, price, menuGroupId, new MenuProducts());
    }

    public void placeMenuProducts(final MenuProducts menuProducts) {
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
        return menuProducts.getMenuProducts();
    }
}
