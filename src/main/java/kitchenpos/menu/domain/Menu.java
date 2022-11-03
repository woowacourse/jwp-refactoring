package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Menu {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private List<MenuProduct> menuProducts;

    public Menu(final Long id, final String name, final BigDecimal price, final Long menuGroupId, List<MenuProduct> menuProducts) {
        validatePrice(price);
        validateProductPriceSum(price, menuProducts);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu(final Long id, final String name, final BigDecimal price, final Long menuGroupId) {
        this(id, name, price, menuGroupId, new ArrayList<>());
    }

    public Menu(final String name, final BigDecimal price, final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validateProductPriceSum(final BigDecimal price, final List<MenuProduct> menuProducts) {
        if (checkExpensiveThanSumOfEachProductPrice(price, menuProducts)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean checkExpensiveThanSumOfEachProductPrice(final BigDecimal price, final List<MenuProduct> menuProducts) {
        if (checkMenuProductSize(menuProducts)) return false;
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            sum = sum.add(menuProduct.getAmount());
        }
        return price.compareTo(sum) > 0;
    }

    private boolean checkMenuProductSize(final List<MenuProduct> menuProducts) {
        if (menuProducts.size() == 0) {
            return true;
        }
        return false;
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
