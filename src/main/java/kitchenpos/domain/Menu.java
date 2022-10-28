package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;

public class Menu {

    private Long id;
    private String name;
    private Price price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    public Menu() {
    }

    public Menu(final String name, final BigDecimal price, final Long menuGroupId, final List<MenuProduct> menuProducts) {
        validateMenuPriceIsLowerTotalPrice(price, menuProducts);
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

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public void setPrice(final BigDecimal price) {
        this.price = new Price(price);
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
