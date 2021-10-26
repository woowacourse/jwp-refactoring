package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;

public class Menu {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    public Menu(String name, Integer price, Long menuGroupId) {
        this(null, name, BigDecimal.valueOf(price), menuGroupId, new ArrayList<>());
    }

    public Menu(String name, Integer price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this(null, name, BigDecimal.valueOf(price), menuGroupId, menuProducts);
    }

    public Menu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    public Menu(long id, String name, BigDecimal price, Long menuGroupId) {
        this(id, name, price, menuGroupId, new ArrayList<>());
    }

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public void addMenuProducts(List<MenuProduct> menuProducts) {
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
