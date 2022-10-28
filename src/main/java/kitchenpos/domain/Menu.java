package kitchenpos.domain;

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

    public Menu(final Long id,
                final String name,
                final BigDecimal price,
                final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("[ERROR] 부적절한 메뉴 가격입니다.");
        }
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts.addAll(menuProducts);
    }

    public static Menu ofNullId(final String name,
                                final BigDecimal price,
                                final Long menuGroupId,
                                final List<MenuProduct> menuProducts) {
        return new Menu(null, name, price, menuGroupId, menuProducts);
    }

    public void addAllMenuProduct(final List<MenuProduct> menuProducts) {
        this.menuProducts.addAll(menuProducts);
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
