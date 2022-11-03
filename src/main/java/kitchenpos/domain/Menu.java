package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Menu {
    private final String name;
    private final Price price;
    private final Long menuGroupId;
    private final Long id;
    private List<MenuProduct> menuProducts;

    public static Menu of(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Price validPrice = new Price(price);
        validatePrice(menuProducts, price);
        return new Menu(null, name, validPrice, menuGroupId, menuProducts);
    }

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId) {
        this(id, name, new Price(price), menuGroupId, new ArrayList<>());
    }

    private Menu(Long id, String name, Price price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    private static void validatePrice(List<MenuProduct> menuProducts, BigDecimal price) {
        BigDecimal sum = calculateSum(menuProducts);
        if (sum.compareTo(price) < 0) {
            throw new IllegalArgumentException("상품의 값의 합보다 메뉴의 값이 낮을 수 없습니다.");
        }
    }

    private static BigDecimal calculateSum(List<MenuProduct> menuProducts) {
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

    public void changeMenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }
}
