package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Menu {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    public Menu(final Long id, final String name, final BigDecimal price, final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu(final Long id, final String name, final BigDecimal price, final Long menuGroupId) {
        this(id, name, price, menuGroupId, new ArrayList<>());
    }

    public void validatePrice(final BigDecimal menuProductPriceSum) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        if (price.compareTo(menuProductPriceSum) > 0) {
            throw new IllegalArgumentException();
        }
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

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
