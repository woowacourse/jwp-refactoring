package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    public Menu(final Long id, final String name, final BigDecimal price, final long menuGroupId) {
        this(id, name, price, menuGroupId, Collections.emptyList());
    }

    public Menu(final Long id, final String name, final BigDecimal price, final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        validatePrice(price);
        validateProductPrice(price, menuProducts);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    private static void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("메뉴 가격은 null이 아니고 0보다 크거나 같아야 합니다.");
        }
    }

    private void validateProductPrice(final BigDecimal price, final List<MenuProduct> menuProducts) {
        final BigDecimal sum = getTotalAmount(menuProducts);
        if (!sum.equals(BigDecimal.ZERO) && price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("상품의 총 합과 메뉴의 총 합이 같지 않습니다.");
        }
    }

    private static BigDecimal getTotalAmount(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProduct::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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
