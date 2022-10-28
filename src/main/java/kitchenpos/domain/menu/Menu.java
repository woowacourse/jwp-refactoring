package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;

public class Menu {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public Menu(final Long id, final String name, final BigDecimal price, final Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public static Menu create(final String name, final Long price, final Long menuGroupId, final List<Long> productIds) {
        final Menu menu = new Menu(null, name, createBigDecimal(price), menuGroupId);
        productIds.forEach(menu::addProduct);
        return menu;
    }

    private static BigDecimal createBigDecimal(final Long price) {
        if (price == null) {
            return null;
        }
        return BigDecimal.valueOf(price);
    }

    private Optional<MenuProduct> findProduct(final Long productId) {
        return menuProducts.stream()
                .filter(menuProduct -> menuProduct.isSameProduct(productId))
                .findAny();
    }

    public void addProduct(final Long productId, final long quantity) {
        final Optional<MenuProduct> menuProduct = findProduct(productId);
        menuProduct.ifPresentOrElse(
                findMenuProduct -> findMenuProduct.addQuantity(quantity),
                () -> menuProducts.add(new MenuProduct(productId, 1))
        );
    }

    public void addProduct(final Long productId) {
        addProduct(productId, 1);
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

    public void bringMenuProducts(final List<MenuProduct> savedMenuProducts) {
        this.menuProducts = savedMenuProducts;
    }
}
