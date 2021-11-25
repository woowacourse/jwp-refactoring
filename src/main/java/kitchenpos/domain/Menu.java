package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class Menu {
    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private List<MenuProduct> menuProducts;

    public Menu(long id, String name, BigDecimal price, long menuGroupId) {
        this(id, name, price, menuGroupId, null);
    }

    public Menu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId,
                List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
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

    public void setMenuIdInProducts(Long menuId) {
        this.menuProducts = menuProducts.stream()
                .map(it -> new MenuProduct(it.getSeq(), menuId, it.getProductId(), it.getQuantity()))
                .collect(Collectors.toList());
    }
}
