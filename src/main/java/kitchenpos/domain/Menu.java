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
    private List<MenuProduct> menuProducts;

    public Menu() {
    }

    private Menu(final Long id, final String name, final BigDecimal price, final Long menuGroupId, final List<MenuProduct> menuProducts) {
        validate(name, price);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    private Menu(final String name, final BigDecimal price, final Long menuGroupId) {
        this(null, name, price, menuGroupId, new ArrayList<>());
    }

    public static Menu create(final String name, final BigDecimal price, final Long menuGroupId) {
        return new Menu(name, price, menuGroupId);
    }

    private void validate(final String name, final BigDecimal price) {
        validateName(name);
        validatePrice(price);
    }

    private void validateName(final String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("메뉴명이 비어있습니다.");
        }
    }

    private void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException("메뉴 가격이 비어있습니다.");
        }

        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("메뉴 가격은 0원 이상이어야 합니다.");
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
