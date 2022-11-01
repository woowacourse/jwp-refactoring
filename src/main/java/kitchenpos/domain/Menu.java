package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Menu {
    private final String name;
    private final Price price;
    private final Long menuGroupId;
    private Long id;
    private List<MenuProduct> menuProducts;

    private Menu(Long id, String name, Price price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId) {
        this(id, name, new Price(price), menuGroupId, new ArrayList<>());
    }

    public Menu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Price validPrice = new Price(price);
        validatePrice(menuProducts, price);
        this.name = name;
        this.price = validPrice;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    private void validatePrice(List<MenuProduct> menuProducts, BigDecimal price) {
        BigDecimal sum = calculateSum(menuProducts);
        if (sum.compareTo(price) < 0) {
            throw new IllegalArgumentException("상품의 값의 합보다 메뉴의 값이 낮을 수 없습니다.");
        }
    }

    private BigDecimal calculateSum(List<MenuProduct> menuProducts) {
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
        return price.getValue();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }
}
