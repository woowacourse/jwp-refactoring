package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;

public class Menu {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    public Menu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        validatePrice(menuProducts, price);
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu() {
    }

    private static BigDecimal calculateSum(List<MenuProduct> menuProducts) {
        return menuProducts.stream().map(MenuProduct::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }

    private void validatePrice(List<MenuProduct> menuProducts, BigDecimal price) {
        BigDecimal sum = calculateSum(menuProducts);
        if (sum.compareTo(price) < 0) {
            throw new IllegalArgumentException("상품의 값의 합보다 메뉴의 값이 낮을 수 없습니다.");
        }
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
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
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
