package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.product.Price;

public class Menu {
    private Long id;
    private String name;
    private Price price;
//    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    public Menu() {
    }

    public Menu(final Long id,
                final String name,
                final BigDecimal price,
                final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        validatePrice(price, menuProducts);
        this.id = id;
        this.name = name;
        this.price = new Price(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu(final String name,
                final BigDecimal price,
                final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    private void validatePrice(final BigDecimal price, final List<MenuProduct> menuProducts) {
        final BigDecimal totalAmount = calculateAmount(menuProducts);
        if (price.compareTo(totalAmount) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private BigDecimal calculateAmount(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProduct::calculateAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    public Long getId() {
        return id;
    }

    @Deprecated
    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Deprecated
    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    @Deprecated
    public void setPrice(final BigDecimal price) {
        this.price = new Price(price);
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    @Deprecated
    public void setMenuGroupId(final Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    @Deprecated
    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }
}
