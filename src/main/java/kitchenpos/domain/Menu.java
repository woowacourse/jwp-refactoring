package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;

public class Menu {
    private Long id;
    private String name;
    private Money price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    public Menu(Long id, String name, Money price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu(Long id, String name, Money price, Long menuGroupId) {
        this(id, name, price, menuGroupId, null);
    }

    public Menu(String name, Money price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    public static Menu of(
            String name,
            BigDecimal price,
            Long menuGroupId,
            List<MenuProduct> menuProducts,
            BigDecimal menuProductTotalPrice
    ) {
        Money priceAmount = Money.valueOf(price);
        validateMenuProductTotalPrice(priceAmount, menuProductTotalPrice);
        return new Menu(name, priceAmount, menuGroupId, menuProducts);
    }

    private static void validateMenuProductTotalPrice(Money price, BigDecimal menuProductTotalPrice) {
        if (price.isGreaterThan(menuProductTotalPrice)) {
            throw new IllegalArgumentException("메뉴 가격은 메뉴 상품 총액을 초과할 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public BigDecimal getPriceValue() {
        return price.getValue();
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
