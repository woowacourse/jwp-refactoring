package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;

public class Menu {

    private Long id;
    private String name;
    private Price price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    public Menu(final Long id,
                final String name,
                final BigDecimal price,
                final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = new Price(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public void checkIsAvailablePrice(final BigDecimal priceToSet) {
        if (price.getPrice().compareTo(priceToSet) > 0) {
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

    public Price getPrice() {
        return price;
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
