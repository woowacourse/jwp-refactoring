package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.domain.Price;

public class Menu {

    private final Long id;
    private final String name;
    private final Price price;
    private final Long menuGroupId;
    private final MenuProducts menuProducts;

    /**
     * DB 에 저장되지 않은 객체
     */
    public Menu(final String name, final BigDecimal price, final Long menuGroupId, final List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
        this.menuProducts.validatePriceIsLowerThanTotalPrice(this.price);
    }

    /**
     * DB 에 저장된 객체
     */
    public Menu(final Long id, final String name, final BigDecimal price, final Long menuGroupId) {
        this(id, name, price, menuGroupId, null);
    }

    public Menu(final Long id, final String name, final BigDecimal price, final Long menuGroupId, final List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = new Price(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts = new MenuProducts(menuProducts);
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
        return menuProducts.getValue();
    }
}
