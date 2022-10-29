package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class Menu {

    private final Long id;
    private final String name;
    private final Price price;
    private final Long menuGroupId;
    private List<MenuProduct> menuProducts;

    /**
     * DB 에 저장되지 않은 객체
     */
    public Menu(final String name, final BigDecimal price, final Long menuGroupId, final List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
        validateMenuPriceIsLowerTotalPrice(price, menuProducts);
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
        this.menuProducts = menuProducts;
    }

    private void validateMenuPriceIsLowerTotalPrice(final BigDecimal price, final List<MenuProduct> menuProducts) {
        final BigDecimal total = getTotal(menuProducts);
        if (price.compareTo(total) > 0) {
            throw new IllegalArgumentException(String.format(
                "메뉴의 가격은 상품의 총 합보다 같거나 작아야 합니다. [Menu Price : %s / total : %s]", price, total
            ));
        }
    }

    private BigDecimal getTotal(final List<MenuProduct> menuProducts) {
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
        return price.getPrice();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }
}
