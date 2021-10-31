package kitchenpos.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts;

    @OneToMany(mappedBy = "menu")
    private List<OrderLineItem> orderLineItems;

    public Menu() {
    }

    private Menu(Builder builder) {
        validatePrice(builder.price);
        validateMenuProductsPrice(builder.menuProducts, builder.price);
        this.id = builder.id;
        this.name = builder.name;
        this.price = builder.price;
        this.menuGroup = builder.menuGroup;
        this.menuProducts = builder.menuProducts;
        registerMenuToMenuProducts(this.menuProducts);
    }

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || (price.compareTo(BigDecimal.ZERO) < 0)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateMenuProductsPrice(List<MenuProduct> menuProducts, BigDecimal price) {
        if (Objects.isNull(menuProducts)) {
            throw new IllegalArgumentException();
        }
        BigDecimal totalPriceOfSingleMenuProduct = calculateMenuProductsPrice(menuProducts);
        if (price.compareTo(totalPriceOfSingleMenuProduct) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private BigDecimal calculateMenuProductsPrice(List<MenuProduct> menuProducts) {
        BigDecimal totalPriceOfSingleMenuProduct = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menuProducts) {
            totalPriceOfSingleMenuProduct = totalPriceOfSingleMenuProduct.add(menuProduct.calculatePrice());
        }
        return totalPriceOfSingleMenuProduct;
    }

    public void registerMenuToMenuProducts(List<MenuProduct> menuProducts) {
        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.registerMenu(this);
        }
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
        if (Objects.isNull(menuGroup)) {
            return null;
        }
        return menuGroup.getId();
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public static class Builder {
        private Long id;
        private String name;
        private BigDecimal price;
        private MenuGroup menuGroup;
        private List<MenuProduct> menuProducts;

        public Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder menuGroup(MenuGroup menuGroup) {
            this.menuGroup = menuGroup;
            return this;
        }

        public Builder menuProducts(List<MenuProduct> menuProducts) {
            this.menuProducts = menuProducts;
            return this;
        }

        public Menu build() {
            return new Menu(this);
        }
    }
}
