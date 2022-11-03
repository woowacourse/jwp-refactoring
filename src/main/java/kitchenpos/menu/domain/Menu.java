package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class Menu {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    public Menu() {
    }

    public Menu(final Long id, final String name, final BigDecimal price, final Long menuGroupId) {
        this(name, price, menuGroupId);
        this.id = id;
    }

    public Menu(final String name, final BigDecimal price, final Long menuGroupId) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public Menu(final String name, final BigDecimal price, final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        this(name, price, menuGroupId);
        this.menuProducts = menuProducts;

        if (isPriceEmptyOrCheaperThan0(price)) {
            throw new IllegalArgumentException("메뉴의 가격은 비어있거나 0보다 작을 수 없습니다.");
        }

        if (isMenuPriceExpensiveThanSumOfEachProductPrice(price)) {
            throw new IllegalArgumentException("가격이 유효하지 않습니다.");
        }
    }

    public Menu(final Long id, final String name, final BigDecimal price, final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    private boolean isPriceEmptyOrCheaperThan0(final BigDecimal price) {
        return Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0;
    }

    private boolean isMenuPriceExpensiveThanSumOfEachProductPrice(final BigDecimal price) {
        return price.compareTo(this.getProductPriceSum()) > 0;
    }

    private BigDecimal getProductPriceSum() {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            sum = sum.add(menuProduct.calculateAmount());
        }
        return sum;
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
