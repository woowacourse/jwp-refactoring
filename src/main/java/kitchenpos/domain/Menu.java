package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class Menu {
    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final MenuProducts menuProducts;

    public Menu(final Long id,
                final String name,
                final BigDecimal price,
                final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        validateMenuPrice(price);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = new MenuProducts(menuProducts);
    }

    private void validateMenuPrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("[ERROR] 부적절한 메뉴 가격입니다.");
        }
    }

    public void compareToTotalMenuPrice(final BigDecimal totalMenuPrice) {
        if (this.price.compareTo(totalMenuPrice) > 0) {
            throw new IllegalArgumentException("[ERROR] 메뉴 가격은 메뉴 상들 가격의 합보다 클 수 없습니다.");
        }
    }

    public static Menu ofNullId(final String name,
                                final BigDecimal price,
                                final Long menuGroupId,
                                final List<MenuProduct> menuProducts) {
        return new Menu(null, name, price, menuGroupId, menuProducts);
    }

    public void addAllMenuProduct(final List<MenuProduct> menuProducts) {
        this.menuProducts.addValues(menuProducts);
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
        return menuProducts.getValues();
    }
}
