package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    public static Menu of(String name, Long price, Long menuGroupId) {
        return new Menu(null, name, createBigDecimal(price), menuGroupId);
    }

    private static BigDecimal createBigDecimal(Long price) {
        if (price == null) {
            return null;
        }
        return BigDecimal.valueOf(price);
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
