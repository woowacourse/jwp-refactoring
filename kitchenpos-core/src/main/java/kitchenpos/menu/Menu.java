package kitchenpos.menu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Menu {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public static Menu of(String name, BigDecimal price, Long menuGroupId, BigDecimal sum) {
        validatePrice(price);
        validateSum(price, sum);
        return new Menu(null, name, price, menuGroupId);
    }

    private static void validateSum(BigDecimal price, BigDecimal sum) {
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private static void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public void addProduct(Long productId, long quantity) {
        menuProducts.add(new MenuProduct(productId, quantity));
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

    public void saveMenuProducts(List<MenuProduct> savedMenuProducts) {
        this.menuProducts = savedMenuProducts;
    }
}
