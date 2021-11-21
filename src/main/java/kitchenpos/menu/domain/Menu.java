package kitchenpos.menu.domain;

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

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    private Menu(Builder builder) {
        validatePrice(builder.price);
        this.id = builder.id;
        this.name = builder.name;
        this.price = builder.price;
        this.menuGroup = builder.menuGroup;
        this.menuProducts = new MenuProducts(builder.menuProducts);
        this.menuProducts.registerMenu(this);
    }

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || (price.compareTo(BigDecimal.ZERO) < 0)) {
            throw new IllegalArgumentException();
        }
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changePrice(BigDecimal price) {
        validatePrice(price);
        this.price = price;
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
        return menuProducts.getMenuProducts();
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
