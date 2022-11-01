package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Menu {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    private Menu() {
    }

    private Menu(final Long id,
                 final String name,
                 final BigDecimal price,
                 final Long menuGroupId,
                 final List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static Menu of(final Long id,
                          final String name,
                          final BigDecimal price,
                          final Long menuGroupId,
                          final List<MenuProduct> menuProducts) {
        validateNegativePrice(price);
        validateSumPrice(price, menuProducts);
        return new Menu(id, name, price, menuGroupId, menuProducts);
    }

    public static Menu of(final String name,
                          final BigDecimal price,
                          final Long menuGroupId,
                          final List<MenuProduct> menuProducts) {
        return Menu.of(null, name, price, menuGroupId, menuProducts);
    }

    public static Menu createForEntity(final Long id,
                                       final String name,
                                       final BigDecimal price,
                                       final Long menuGroupId) {
        return new Menu(id, name, price, menuGroupId, new LinkedList<>());
    }

    private static void validateSumPrice(final BigDecimal price, final List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menuProducts) {
            sum = sum.add(menuProduct.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴의 가격이 메뉴 상품의 가격의 합을 넘을 수 없습니다.");
        }
    }

    private static void validateNegativePrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("메뉴의 가격은 음수일 수 없습니다.");
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

    public void addMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts.addAll(menuProducts);
    }
}
