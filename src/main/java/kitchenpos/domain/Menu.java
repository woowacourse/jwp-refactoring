package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class Menu {

    private final Long id;
    private final String name;
    private final Price price;
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
        validateProductPrice(price, menuProducts);
        this.id = id;
        this.name = name;
        this.price = new Price(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
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

    public Price getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
