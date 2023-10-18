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

    private Menu(final Long id,
                 final String name,
                 final BigDecimal price,
                 final Long menuGroupId,
                 final List<MenuProduct> menuProducts) {
        validatePrice(price);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static Menu of(final String name,
                          final Long price,
                          final Long menuGroupId) {
        validateNull(price);
        return new Menu(null, name, BigDecimal.valueOf(price), menuGroupId, new ArrayList<>());
    }

    public static Menu of(final String name,
                          final Long price,
                          final Long menuGroupId,
                          final List<MenuProduct> menuProducts) {
        validateNull(price);
        return new Menu(null, name, BigDecimal.valueOf(price), menuGroupId, menuProducts);
    }

    public static Menu of(final Long id,
                          final String name,
                          final BigDecimal price,
                          final Long menuGroupId) {
        return new Menu(id, name, price, menuGroupId, new ArrayList<>());
    }

    private static void validateNull(final Long price) {
        if (price == null) {
            throw new IllegalArgumentException("메뉴의 가격은 null일 수 없습니다.");
        }
    }

    private void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public boolean isGreaterThanByPrice(final BigDecimal other) {
        return price.compareTo(other) > 0;
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

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }
}
