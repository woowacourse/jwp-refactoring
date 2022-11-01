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

//    public Menu(final Long id, final String name, final BigDecimal price, final Long menuGroupId,
//                final List<MenuProduct> menuProducts) {
//        this.id = id;
//        this.name = name;
//        this.price = price;
//        this.menuGroupId = menuGroupId;
//        this.menuProducts = new ArrayList<>(menuProducts);
//    }

    public Menu(final Long id, final String name, final BigDecimal price, final Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public Menu(final String name, final BigDecimal price, final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        validatePrice(price);
        validateMenuProductPriceGreaterThan(price, menuProducts);
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = new ArrayList<>(menuProducts);
    }

    private void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validateMenuProductPriceGreaterThan(final BigDecimal price, final List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;

        for (final MenuProduct menuProduct : menuProducts) {
            sum = sum.add(menuProduct.calculateMultiplyPrice());
        }
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public List<MenuProduct> getMenuProducts() {
        return new ArrayList<>(menuProducts);
    }

    public void changeMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = new ArrayList<>(menuProducts);
    }
}
