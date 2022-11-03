package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import kitchenpos.product.domain.Product;

public class Menu {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProduct> menuProducts;

    public Menu(final String name, final BigDecimal price, final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    public Menu(final Long id, final String name, final BigDecimal price, final Long menuGroupId) {
        this(id, name, price, menuGroupId, new ArrayList<>());
    }

    public Menu(final Long id, final String name, final BigDecimal price, final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public BigDecimal calculateTotalSum(final Map<Long, Product> products) {
        return menuProducts.stream()
                .map(it -> {
                    Product product = products.get(it.getProductId());
                    return product.multiplePrice(it.getQuantity());
                }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts.addAll(menuProducts);
    }

    public boolean isBiggerPrice(final BigDecimal value) {
        return price.compareTo(value) > 0;
    }

    public void changeMenuIdInMenuProducts() {
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.changeMenuId(id);
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
