package kitchenpos.domain;

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

    public Menu(final String name, final BigDecimal price, final Long menuGroupId) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public Menu(final String name, final BigDecimal price, final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;

        if (isPriceEmpty(price)) {
            throw new IllegalArgumentException("메뉴의 가격은 비어있거나 0보다 작을 수 없습니다.");
        }

        if (isMenuPriceExpensiveThanSumOfEachProductPrice(price)) {
            throw new IllegalArgumentException("가격이 유효하지 않습니다.");
        }
    }

    private boolean isPriceEmpty(final BigDecimal price) {
        return Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0;
    }

    private boolean isMenuPriceExpensiveThanSumOfEachProductPrice(final BigDecimal price) {
        return price.compareTo(this.getProductPriceSum()) > 0;
    }

    private BigDecimal getProductPriceSum() {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            sum = sum.add(menuProduct.getAmount());
        }
        return sum;
    }

    public Long getId() {
        return id;
    }

    @Deprecated
    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Deprecated
    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Deprecated
    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    @Deprecated
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
