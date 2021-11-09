package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private Long menuGroupId;
    @Embedded
    private MenuProducts menuProducts;

    public Menu() {
    }

    private Menu(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.price = builder.price;
        this.menuGroupId = builder.menuGroupId;
        this.menuProducts = builder.menuProducts;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void updateMenuProducts(MenuProducts menuProducts) {
        this.menuProducts = menuProducts;
    }

    public void updateMenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = new MenuProducts(menuProducts);
    }

    public static class Builder {
        private Long id;
        private String name;
        private BigDecimal price;
        private Long menuGroupId;
        private MenuProducts menuProducts;

        private Builder() {
        }

        public Builder of(Menu menu) {
            this.id = menu.id;
            this.name = menu.name;
            this.price = menu.price;
            this.menuGroupId = menu.menuGroupId;
            this.menuProducts = menu.menuProducts;
            return this;
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

        public Builder menuGroupId(Long menuGroupId) {
            this.menuGroupId = menuGroupId;
            return this;
        }


        public Menu build() {
            return new Menu(this);
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
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }
}
